package com.dsfy.service.impl;

import com.dsfy.dao.util.Pagination;
import com.dsfy.dao.util.QueryCondition;
import com.dsfy.entity.Message;
import com.dsfy.entity.RentalInfo;
import com.dsfy.entity.authority.SysmanUser;
import com.dsfy.entity.http.JsonResponse;
import com.dsfy.entity.pay.CashCoupon;
import com.dsfy.entity.pay.Order;
import com.dsfy.entity.pay.OrderDetail;
import com.dsfy.exception.JsonException;
import com.dsfy.refund.ws.WSHelper;
import com.dsfy.refund.ws.WSProxy;
import com.dsfy.service.IOrderService;
import com.dsfy.util.Constant;
import com.dsfy.util.ValidateUtil;
import com.google.gson.JsonIOException;
import com.sdo.mas.common.api.refund.entity.RefundRequest;
import com.sdo.mas.common.api.refund.entity.RefundResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("OrderService")
public class OrderService extends BaseService implements IOrderService {

    /**
     * 下单
     *
     * @param order
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public JsonResponse order(Order order) {
        // 1. 检查租赁信息是否存在
        RentalInfo rentalInfo = baseDao.getById(RentalInfo.class, order.getRentalId());
        if (rentalInfo == null) {
            throw new JsonIOException("租赁信息不存在");
        }
        if (rentalInfo.getUserId() == 0) {
            throw new JsonIOException("租赁信息不完整");
        }
        if (rentalInfo.getLocked() == RentalInfo.Locked.locked) {
            throw new JsonIOException("租赁信息已经被锁定");
        }
        if (rentalInfo.getVerify() != RentalInfo.Verify.verify) {
            throw new JsonIOException("租赁信息未通过审核");
        }
        order.setShare(Order.Shared.shared);//未分享
        order.setCover(rentalInfo.getCover());//封面
        order.setModel(rentalInfo.getModel());//型号
        order.setBrand(rentalInfo.getBrand());//品牌
        order.setAddress(rentalInfo.getCity() + rentalInfo.getAddress());//收货地址

        // 2. 验证租客信息
        SysmanUser guest = baseDao.getById(SysmanUser.class, order.getUserId());
        if (guest == null) {
            throw new JsonIOException("您的注册信息不存在");
        }
        if (guest.getLocked() != SysmanUser.Locked.unlocked) {
            throw new JsonIOException("您的账号已经被锁定,不能进行此操作");
        }
        if (guest.getVerify() != SysmanUser.Verify.verify) {
            throw new JsonIOException("您的账号未通过认证,不能进行此操作");
        }
        order.setUserId(guest.getPid());//订单租客ID
        order.setName(guest.getRealName());//订单租客姓名
        order.setNickName(guest.getNickname());//订单租客昵称

        //3. 验证机主信息(注意这里取的是租赁信息里面的机主(用户))
        SysmanUser admin = baseDao.getById(SysmanUser.class, rentalInfo.getUserId());
        if (admin == null) {
            throw new JsonIOException("机主注册信息不存在");
        }
        if (admin.getVerify() != SysmanUser.Verify.verify) {
            throw new JsonIOException("机主信息未通过审核,不能进行此操作");
        }
        if (admin.getLocked() != SysmanUser.Locked.unlocked) {
            throw new JsonIOException("机主信息已经被锁定,不能进行此操作");
        }
        order.setRentalUserId(admin.getPid());//机主ID
        order.setRentalName(admin.getRealName());//机主姓名
        order.setRentalNickName(admin.getNickname());//机主昵称

        // 4. 计算租赁天数
        int d = calculateRentalDays(order.getObtainTime(), order.getReturnTime());
        order.setRentalDays(d);
        // 计算租金
        double rentalAmount = calculateRental(d, rentalInfo.getPrice());//租金(不含保险)
        double insurance = (rentalAmount * 0.05);//强制保险
        double deductibleInsurance = 0;//免赔险
        if (order.getDeductibleInsurance() > 0) {//如果选择了免赔险
            deductibleInsurance = (rentalAmount * 0.15);
        }
        // 将各种金额写入订单
        order.setCashAmount(0);//代金券金额
        order.setInsurance(insurance);//强制保险
        order.setDeductibleInsurance(deductibleInsurance);//免赔险
        order.setDeposit(rentalInfo.getDeposit());//押金
        order.setRentalAmount(rentalAmount);//租金(不含保险)
        order.setAmount(rentalAmount + insurance + deductibleInsurance + rentalInfo.getDeposit());//订单总金额=押金+租金+强制保险+免赔险
        order.setPrice(rentalInfo.getPrice());//租赁价格
        order.setRefund(0);// 退款
        order.setPayAmount(0);//支付的现金金额

        // 5. 生成订单
        order.setOrderNumber(generateOrderNumber());//订单编号
        order.setState(Order.State.ordered);// 订单状态下单
        order.setDeleted(Order.DeleteState.unDelete);//未删除
        order.setCreateTime(new Date().getTime());//订单创建时间
        if (rentalInfo.getAutoAccept() == RentalInfo.AutoAccept.yes) {//如果设置了自动接单
            order.setAccetpTime(new Date().getTime());//接单时间
            order.setState(Order.State.accepted);
        }

        // 6. 查询该租赁信息正在出租的信息,验证该时间段是否允许租机
        List<QueryCondition> conditions = new ArrayList<>();
        conditions.add(new QueryCondition("rentalId", QueryCondition.EQ, order.getRentalId()));//租赁信息ID
        conditions.add(new QueryCondition("state", QueryCondition.LT, Order.State.waitingRefundCallBack));//未完成的订单
        List<Order> orders = get(Order.class, conditions);

        // 这次下单的时间区间
        long start = ValidateUtil.dayOfStar(order.getObtainTime());
        long end = ValidateUtil.dayOfEnd(order.getReturnTime());
        if (orders != null && orders.size() > 0) {
            for (Order order1 : orders) {
                // 真实的
                long hasObtainTime = order1.getRealObtainTime();
                // 真实的
                long hasReturnTime = order1.getRealReturnTime();
                // 如果新的订单的 取机时间 在已有订单的 取机时间 和还机时间 区间内
                //或者 如果新的订单的 还机时间 在已有订单的 取机时间 和还机时间 区间内
                if (start < hasObtainTime && hasObtainTime < end || start < hasReturnTime && hasReturnTime < end) {
                    throw new JsonIOException("您选择的时间段,该相机正在出租");
                }
                // 原来的
                /*if (start < order1.getObtainTime() && order1.getObtainTime() < end ||
                        start < order1.getReturnTime() && order1.getReturnTime() < end) {
                    throw new JsonIOException("您选择的时间段,该相机正在出租");
                }*/
            }
        }

        // 1.生成订单详情--下单
        OrderDetail detail = new OrderDetail();
        detail.setOrderNumber(order.getOrderNumber());
        detail.setTime(new Date().getTime());
        detail.setDescription("订单已经生成");
        detail.setUserId(order.getUserId());
        detail.setName(order.getName());
        baseDao.save(detail);

        //如果设置了自动接单,那么设置订单状态为已接受
        if (rentalInfo.getAutoAccept() == RentalInfo.AutoAccept.yes) {
            // 1.生成订单详情--自动接单
            detail = new OrderDetail();
            detail.setOrderNumber(order.getOrderNumber());
            detail.setUserId(0);
            detail.setName("系统自动处理");
            detail.setTime(new Date().getTime());//接受订单时间
            detail.setDescription("机主接自动受订单");//描述
            baseDao.save(detail);

            // 自动接单后添加--支付超时检测
            Message message = new Message();
            message.setIsFinish(Message.IsFinish.unFinish);
            message.setOrderNumber(order.getOrderNumber());
            message.setCategory(Message.Category.checkOrderPay);
            message.setParameters("支付超时检测");
            message.setGuestPhone(guest.getPhone());//租客电话
            message.setAdminPhone(admin.getPhone());//机主电话
            addMessage(message);

        } else {
            //如果没有设置自动接单,添加--接单超时检查
            // 机主接受订单超时检测
            Message message = new Message();
            message.setIsFinish(Message.IsFinish.unFinish);
            message.setOrderNumber(order.getOrderNumber());
            message.setCategory(Message.Category.checkOrderAccept);
            message.setParameters("接单超时检查");
            message.setGuestPhone(guest.getPhone());//租客电话
            message.setAdminPhone(admin.getPhone());//机主电话
            addMessage(message);

            // 租客已经下单,通知租客
            message = new Message();
            message.setIsFinish(Message.IsFinish.unFinish);
            message.setOrderNumber(order.getOrderNumber());
            message.setCategory(Message.Category.ordered);
            message.setIsFinish(Message.IsFinish.unFinish);
            message.setParameters("下单成功通知");
            message.setSendTime(new Date().getTime());//立刻执行
            message.setGuestPhone(guest.getPhone());//租客电话
            message.setAdminPhone(admin.getPhone());//机主电话
            baseDao.save(message);
        }
        baseDao.save(order);

        JsonResponse jsonResponse = JsonResponse.success("订单已生成");
        jsonResponse.setData("order", order);
        return jsonResponse;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void accept(Order order) {
        // 1. 接受订单
        order.setAccetpTime(new Date().getTime());
        order.setState(Order.State.accepted);
        baseDao.update(order);

        // 2. 添加订单处理详情
        OrderDetail detail = new OrderDetail();
        detail.setOrderNumber(order.getOrderNumber());
        detail.setTime(new Date().getTime());
        detail.setDescription("机主接受订单");
        detail.setUserId(order.getRentalUserId());
        detail.setName(order.getRentalName());
        baseDao.save(detail);

        // 3. 添加消息处理
        SysmanUser guest = baseDao.getById(SysmanUser.class, order.getUserId());
        if (guest == null) {
            throw new JsonException("租客信息不存在");
        }
        SysmanUser admin = baseDao.getById(SysmanUser.class, order.getRentalUserId());
        if (admin == null) {
            throw new JsonException("机主信息不存在");
        }
        // 添加支付超时检测(15分钟)
        /** 支付超时发送确认短信给机主，提醒订单取消。*/
        Message message = new Message();
        message.setIsFinish(Message.IsFinish.unFinish);
        message.setGuestPhone(guest.getPhone());//租客电话
        message.setAdminPhone(admin.getPhone());//机主电话
        message.setOrderNumber(order.getOrderNumber());//订单编号
        message.setParameters("支付超时检测");
        message.setCategory(Message.Category.checkOrderPay);//检查类型为支付超时检查
        addMessage(message);

        // 通知租客支付订单(45)分钟
        /** 短信提醒租客支付押金或重新选择 */
        message = new Message();
        message.setIsFinish(Message.IsFinish.unFinish);
        message.setOrderNumber(order.getOrderNumber());
        message.setCategory(Message.Category.accepted);
        message.setParameters("机主接受订单通知");
        message.setSendTime(new Date().getTime());//立刻执行
        message.setGuestPhone(guest.getPhone());//租客电话
        message.setAdminPhone(admin.getPhone());//机主电话
        baseDao.save(message);
    }

    /**
     * 支付之前调用,用来处理代金券,和需要支付的现金金额
     * 更新订单状态为 等待支付回调
     *
     * @param orderNumber 订单编号
     * @param cashId      代金券编号
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void payment(String orderNumber, int cashId) {
        // 1. 校验订单信息
        Order order = baseDao.getById(Order.class, orderNumber);
        if (order == null) {
            throw new JsonException("支付的订单不存在");
        }
        // 如果已经调用过
        if (order.getState() == Order.State.waitingPaymentCallBack) {
            throw new JsonException("订单正在处理请勿重复请求:" + order.getState());
        }
        if (order.getState() == Order.State.paymentTimeout) {
            throw new JsonException("您超过45分钟未支付订单,订单支付超时:" + order.getState());
        }
        // 只有机主已经接单的订单才允许支付
        if (order.getState() != Order.State.accepted) {
            throw new JsonException("订单状态不正确不能执行此操作:" + order.getState());
        }

        // 2. 处理代金券 作废代金券 更新订单使用的代金券金额为代金券金额
        double cashAmount = 0;
        if (cashId != 0) {
            CashCoupon cashCoupon = baseDao.getById(CashCoupon.class, cashId);
            if (cashCoupon == null) {
                throw new JsonException("代金券不存在");
            }
            if (cashCoupon.getExpireTime() < new Date().getTime()) {
                throw new JsonException("代金券已经过期,不能使用");
            }
            if (cashCoupon.getState() != CashCoupon.State.enable) {
                throw new JsonException("代金券已经作废,不能使用");
            }
            cashAmount = cashCoupon.getAmount();
            cashCoupon.setState(CashCoupon.State.disable);
            cashCoupon.setUseTime(new Date().getTime());
            cashCoupon.setOrderNumber(order.getOrderNumber());
            baseDao.update(cashCoupon);
        }

        // 3. 更新订单信息,订单支付的现金和使用的代金券的金额
        order.setCashId(cashId);//代金券编号
        order.setCashAmount(cashAmount);//代金券金额
        order.setPayAmount(order.getAmount() - cashAmount);//支付现金金额
        // 用户支付请求后,将订单修改为--等待支付回调
        if (order.getState() != Order.State.paymented) {//如果支付回调还没有执行
            order.setState(Order.State.waitingPaymentCallBack);
        }
        baseDao.update(order);

        // 4. 添加订单处理详情
        OrderDetail detail = new OrderDetail();
        detail.setOrderNumber(order.getOrderNumber());
        detail.setTime(new Date().getTime());
        detail.setDescription("等待支付回调,订单金额:" + order.getAmount() + ",需支付--現金:" + order.getPayAmount() + "--代金券:" + order.getCashAmount());
        detail.setUserId(order.getUserId());
        detail.setName(order.getName());
        baseDao.save(detail);

        // 5. 添加等待支付消息处理
        SysmanUser guest = baseDao.getById(SysmanUser.class, order.getUserId());
        if (guest == null) {
            throw new JsonException("租客信息不存在");
        }
        SysmanUser admin = baseDao.getById(SysmanUser.class, order.getRentalUserId());
        if (admin == null) {
            throw new JsonException("机主信息不存在");
        }

        // 等待支付回调
        Message message = new Message();
        message.setIsFinish(Message.IsFinish.unFinish);
        message.setOrderNumber(order.getOrderNumber());
        message.setCategory(Message.Category.waitingPaymentCallBack);
        message.setSendTime(new Date().getTime());
        message.setGuestPhone(guest.getPhone());//租客电话
        message.setAdminPhone(admin.getPhone());//机主电话
        message.setTemplateId("id");
        message.setParameters("发起支付更新，代金券，支付现金金额");
        baseDao.save(message);
    }

    /**
     * 盛付通支付完成后的回调
     *
     * @param orderNumber
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void paymentCallBack(String orderNumber) {
        // 1. 更新订单信息
        Order order = baseDao.getById(Order.class, orderNumber);
        if (order == null) {
            return;
        }
        // 如果订单已经处理了支付回调,则不再处理,如果支付和支付回调中间被修改为支付超时,此时是需要处理回调的
        if (order.getState() >= Order.State.paymented && order.getState() != Order.State.paymentTimeout) {
            return;
        }
        order.setState(Order.State.paymented);
        baseDao.save(order);

        // 2. 支付完成,添加各种消息检测
        SysmanUser guest = baseDao.getById(SysmanUser.class, order.getUserId());
        if (guest == null) {
            throw new JsonException("租客信息不存在");
        }
        SysmanUser admin = baseDao.getById(SysmanUser.class, order.getRentalUserId());
        if (admin == null) {
            throw new JsonException("机主信息不存在");
        }
        // 取机时间通知
        /** 交接当日早上发送短信给双方提醒相关事宜 */
        Message message = new Message();
        message.setIsFinish(Message.IsFinish.unFinish);
        message.setOrderNumber(order.getOrderNumber());
        message.setCategory(Message.Category.pushObtain);
        message.setSendTime(order.getObtainTime() - 60 * 60 * 1000);//提前一小时
        message.setGuestPhone(guest.getPhone());//租客电话
        message.setAdminPhone(admin.getPhone());//机主电话
        message.setTemplateId("id");
        message.setParameters("取机时间短信通知");
        addMessage(message);

        // 支付完成消息处理
        /** 支付成功发送确认短信给双方，包含型号，交机时间，还机时间，交接地点，机主/客户联系方式 */
        message = new Message();
        message.setIsFinish(Message.IsFinish.unFinish);
        message.setOrderNumber(order.getOrderNumber());
        message.setCategory(Message.Category.paymented);
        message.setSendTime(new Date().getTime());//立即执行
        message.setGuestPhone(guest.getPhone());//租客电话
        message.setAdminPhone(admin.getPhone());//机主电话
        message.setTemplateId("id");
        message.setParameters("支付完成通知");
        baseDao.save(message);

        // 保存支付完成信息
        OrderDetail detail = new OrderDetail();
        detail.setOrderNumber(orderNumber);
        detail.setTime(new Date().getTime());
        detail.setDescription("支付完成,支付金额:" + order.getPayAmount());
        detail.setName("盛付通");
        baseDao.save(detail);
    }

    /**
     * 机主交出相机后,请求
     *
     * @param order
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void obtainedNotice(Order order) {
        // 机主已经交出相机
        order.setState(Order.State.obtainedNotice);
        baseDao.update(order);

        // 添加订单处理详情
        OrderDetail detail = new OrderDetail();
        detail.setOrderNumber(order.getOrderNumber());
        detail.setTime(new Date().getTime());
        detail.setDescription("机主已经交出相机");
        detail.setUserId(order.getRentalUserId());
        detail.setName(order.getRentalName());
        baseDao.save(detail);

        // 交出相机通知处理
        SysmanUser guest = baseDao.getById(SysmanUser.class, order.getUserId());
        if (guest == null) {
            throw new JsonException("租客信息不存在");
        }
        SysmanUser admin = baseDao.getById(SysmanUser.class, order.getRentalUserId());
        if (admin == null) {
            throw new JsonException("机主信息不存在");
        }

        // 取机通知
        Message message = new Message();
        message.setIsFinish(Message.IsFinish.unFinish);
        message.setOrderNumber(order.getOrderNumber());
        message.setCategory(Message.Category.obtainedNotice);
        message.setParameters("机主通知租客取机");
        message.setSendTime(new Date().getTime());
        message.setGuestPhone(guest.getPhone());//租客电话
        message.setAdminPhone(admin.getPhone());//机主电话
        baseDao.save(message);
    }

    /**
     * 租客收到相机后调用
     *
     * @param order
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void obtained(Order order) {
        // 1. 更新订单状态
        order.setState(Order.State.obtained);
        baseDao.update(order);

        // 2. 添加订单处理详情
        OrderDetail detail = new OrderDetail();
        detail.setOrderNumber(order.getOrderNumber());
        detail.setTime(new Date().getTime());
        detail.setDescription("租客已经取机");
        detail.setUserId(order.getUserId());
        detail.setName(order.getName());
        baseDao.save(detail);

        // 订单支付之后设定--取机时间,还机时间通知
        SysmanUser guest = baseDao.getById(SysmanUser.class, order.getUserId());
        if (guest == null) {
            throw new JsonException("租客信息不存在");
        }
        SysmanUser admin = baseDao.getById(SysmanUser.class, order.getRentalUserId());
        if (admin == null) {
            throw new JsonException("机主信息不存在");
        }

        // 租客拿到相机通知
        Message message = new Message();
        message.setIsFinish(Message.IsFinish.unFinish);
        message.setOrderNumber(order.getOrderNumber());
        message.setCategory(Message.Category.obtained);
        message.setParameters("租客拿到相机通知");
        message.setSendTime(new Date().getTime());
        message.setGuestPhone(guest.getPhone());//租客电话
        message.setAdminPhone(admin.getPhone());//机主电话
        baseDao.save(message);

        // 还机时间通知
        /** 归还日前一天短信提醒归还时间，地点。并提醒请准时归还。*/
        message = new Message();
        message.setIsFinish(Message.IsFinish.unFinish);
        message.setOrderNumber(order.getOrderNumber());
        message.setCategory(Message.Category.pushReturn);
        message.setSendTime(order.getReturnTime() - 60 * 60 * 1000);//提前一小时
        message.setGuestPhone(guest.getPhone());//租客电话
        message.setAdminPhone(admin.getPhone());//机主电话
        message.setTemplateId("id");
        message.setParameters("还机时间短信通知");
        addMessage(message);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void returnedNotice(Order order) {
        // 1. 更新订单状态
        order.setState(Order.State.returnedNotice);
        baseDao.update(order);

        // 2. 添加订单处理详情
        OrderDetail detail = new OrderDetail();
        detail.setOrderNumber(order.getOrderNumber());
        detail.setTime(new Date().getTime());
        detail.setDescription("租客申请还机");
        detail.setUserId(order.getUserId());
        detail.setName(order.getName());
        baseDao.save(detail);

        // 3. 租客还机通知
        SysmanUser guest = baseDao.getById(SysmanUser.class, order.getUserId());
        if (guest == null) {
            throw new JsonException("租客信息不存在");
        }
        SysmanUser admin = baseDao.getById(SysmanUser.class, order.getRentalUserId());
        if (admin == null) {
            throw new JsonException("机主信息不存在");
        }

        Message message = new Message();
        message.setIsFinish(Message.IsFinish.unFinish);
        message.setOrderNumber(order.getOrderNumber());
        message.setCategory(Message.Category.returnedNotice);
        message.setParameters("租客还机通知");
        message.setSendTime(new Date().getTime());
        message.setGuestPhone(guest.getPhone());//租客电话
        message.setAdminPhone(admin.getPhone());//机主电话
        baseDao.save(message);
    }

    /**
     * 相机归还之后更新,租赁次数, 平均接单响应时间
     * @param order
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void returned(Order order) {
        // 1.更新订单--机主确认归还
        order.setState(Order.State.returned);
        baseDao.update(order);

        // 2.订单处理详情
        OrderDetail detail = new OrderDetail();
        detail.setOrderNumber(order.getOrderNumber());
        detail.setTime(new Date().getTime());
        detail.setDescription("机主确认还机");
        detail.setUserId(order.getRentalUserId());
        detail.setName(order.getRentalName());
        baseDao.save(detail);

        // 3.更新租赁信息,租赁次数, 平均响应时间
        RentalInfo rentalInfo = baseDao.getById(RentalInfo.class, order.getRentalId());
        if (rentalInfo != null) {
            rentalInfo.setRentalCount(rentalInfo.getRentalCount() + 1);
        }
        // 更新平均响应时间
        String sql = "SELECT AVG(o.accetpTime - o.createTime ) FROM Order o WHERE o.state <> ?  AND o.state <> ?  AND o.state <> ?  AND o.state <> ? AND o.rentalId = ?";
        Object result = baseDao.getUniqueResultByJpql(sql, Order.State.ordered, Order.State.reject, Order.State.acceptTimeout, Order.State.paymentTimeout, rentalInfo.getRentalId());
        long responstTime = 0L;
        if (result != null) {
            responstTime = ((Double) result).longValue();
        }
        rentalInfo.setResponseTime(responstTime < 0 ? 0 : responstTime);// 平均响应时间
        baseDao.update(rentalInfo);

        // 4. 租客还机后通知
        SysmanUser guest = baseDao.getById(SysmanUser.class, order.getUserId());
        if (guest == null) {
            throw new JsonException("租客信息不存在");
        }
        SysmanUser admin = baseDao.getById(SysmanUser.class, order.getRentalUserId());
        if (admin == null) {
            throw new JsonException("机主信息不存在");
        }

        Message message = new Message();
        message.setIsFinish(Message.IsFinish.unFinish);
        message.setOrderNumber(order.getOrderNumber());
        message.setCategory(Message.Category.returned);
        message.setParameters("机主确认还机通知");
        message.setSendTime(new Date().getTime());
        message.setGuestPhone(guest.getPhone());//租客电话
        message.setAdminPhone(admin.getPhone());//机主电话
        baseDao.save(message);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void refund(String orderNumber) {
        // 1. 订单状态检测
        Order order = baseDao.getById(Order.class, orderNumber);
        if (order == null) {
            throw new JsonException("更新的订单不存在");
        }
        if (order.getState() < Order.State.returned) {
            throw new JsonException("机主还未确认还机,不能退款:" + order.getState());
        }

        if (order.getState() != Order.State.returned) {
            throw new JsonException("订单状态有误,不能退款:" + order.getState());
        }


        SysmanUser guest = baseDao.getById(SysmanUser.class, order.getUserId());
        if (guest == null) {
            throw new JsonException("租客信息不存在");
        }
        SysmanUser admin = baseDao.getById(SysmanUser.class, order.getRentalUserId());
        if (admin == null) {
            throw new JsonException("机主信息不存在");
        }

        // 2. 订单金额变化值,退还给租客的金额,退还给机主的金额计算
        // 计算订单时间变更后增加的应该支付(扣除)的金额, 订单金额增加量,修改时间导致订单时间增加,所以需要多扣除部分钱
        double addedAmount = 0;
        if (order.getNewAmount() > 0) {//如果更新过时间
            addedAmount = order.getNewAmount() - order.getAmount();
        }

        // 退还给租客的钱 (订单总金额 = 押金 + 租金 + 强制保险 + 免赔险  退款金额 = 订单总金额 - 租金 - 强制保险 - 免赔险)
        double refound = order.getAmount() - order.getRentalAmount() - order.getInsurance() - order.getDeductibleInsurance();
        if (addedAmount > 0) {// 如果订单金额增加了,把本来需要多支付的部分从押金扣除
            refound = refound - addedAmount;
        }

        // 退还给机主的钱
        double rentalRefund = Math.max(order.getRentalAmount(), order.getNewRentalAmount()) * 0.9;
        // 如果退款金额大于现金
        if (refound + rentalRefund > order.getPayAmount()) {
            throw new JsonException("退款金额总和大于了,支付金额");
        }

        // 3. 更新为等待退款回调
        order.setRefund(refound);//租客退还金额
        order.setRefundRental(rentalRefund);//机主退还金额
        order.setState(Order.State.waitingRefundCallBack);
        baseDao.update(order);

        // 4. 将钱打入机主余额
        admin.setAmount(admin.getAmount() + rentalRefund);
        baseDao.update(admin);

        // 5. 消息处理
        Message message = new Message();
        message.setIsFinish(Message.IsFinish.unFinish);
        message.setOrderNumber(order.getOrderNumber());
        message.setCategory(Message.Category.waitingRefundCallBack);
        message.setParameters("等待退款回调通知");
        message.setSendTime(new Date().getTime());
        message.setGuestPhone(guest.getPhone());//租客电话
        message.setAdminPhone(admin.getPhone());//机主电话
        baseDao.save(message);

        // 订单处理详情
        OrderDetail detail = new OrderDetail();
        detail.setOrderNumber(order.getOrderNumber());
        detail.setTime(new Date().getTime());
        detail.setDescription("退款请求已提交,等待退款回调");
        detail.setUserId(order.getUserId());
        detail.setName(order.getName());
        baseDao.save(detail);

        // 订单总金额 = 押金 + 租金 + 强制保险 + 免赔险  退款金额 = 订单总金额- 租金 - 强制保险 - 免赔险
        RefundRequest refundRequest = WSHelper.getInitRefundRequest();
        refundRequest.setMemo("正常订单退款");//备注
        refundRequest.setMerchantNo(Constant.merchantNo);//商户号
        refundRequest.setNotifyURL(Constant.refundNotifyUrl);//回调URL
        refundRequest.setOriginalOrderNo(order.getOrderNumber());//原始订单号
        // TODO 退款金额正式发布时候需要改为真实值
        refundRequest.setRefundAmount("0.01");//退款金额
        // refundRequest.setRefundAmount(new DecimalFormat("#.00").format(refound));//退款金额
        refundRequest.setRefundOrderNo("" + new Date().getTime());//退款订单号

        WSProxy proxy = new WSProxy();
        try {
            // 签名
            WSHelper.signRefundRequest(refundRequest, Constant.merchantKey);
            RefundResponse response = proxy.doRefund(refundRequest);
            if (response.getStatus() == null || response.getStatus().equals("02")) {// 00:处理中;01：成功;02:失败;
                throw new JsonException(response.getReturnInfo() == null ? "未知错误" : response.getReturnInfo().getErrorMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new JsonException("退款失败:" + e.getMessage());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void refundCallback(String orderNumbar) {
        // 1. 订单状态校验
        Order order = baseDao.getById(Order.class, orderNumbar);
        if (order == null) {
            throw new JsonException("更新的订单不存在");
        }
        if (order.getState() == Order.State.refunded) {
            return;
        }
        // 更新状态为已经退款完成
        order.setState(Order.State.refunded);
        baseDao.update(order);

        // 2. 订单处理详情
        OrderDetail detail = new OrderDetail();
        detail.setOrderNumber(orderNumbar);
        detail.setTime(new Date().getTime());
        detail.setDescription("退款完成");
        detail.setName("盛付通");
        baseDao.save(detail);

        // 3. 退款成功通知
        SysmanUser guest = baseDao.getById(SysmanUser.class, order.getUserId());
        if (guest == null) {
            throw new JsonException("租客信息不存在");
        }
        SysmanUser admin = baseDao.getById(SysmanUser.class, order.getRentalUserId());
        if (admin == null) {
            throw new JsonException("机主信息不存在");
        }

        // 4. 退款消息通知
        Message message = new Message();
        message.setIsFinish(Message.IsFinish.unFinish);
        message.setOrderNumber(order.getOrderNumber());
        message.setCategory(Message.Category.refunded);
        message.setParameters("退款成功通知");
        message.setSendTime(new Date().getTime());
        message.setGuestPhone(guest.getPhone());//租客电话
        message.setAdminPhone(admin.getPhone());//机主电话
        baseDao.save(message);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void close(Order order) {
        // 已经评论,关闭订单
        order.setState(Order.State.isclosed);
        baseDao.update(order);
        // 添加订单处理详情
        OrderDetail detail = new OrderDetail();
        detail.setOrderNumber(order.getOrderNumber());
        detail.setTime(new Date().getTime());
        detail.setDescription("租客已经评论");
        detail.setUserId(order.getUserId());
        detail.setName(order.getName());
        baseDao.save(detail);

        // 订单支付之后设定--取机时间,还机时间通知
        SysmanUser guest = baseDao.getById(SysmanUser.class, order.getUserId());
        if (guest == null) {
            throw new JsonException("租客信息不存在");
        }
        SysmanUser admin = baseDao.getById(SysmanUser.class, order.getRentalUserId());
        if (admin == null) {
            throw new JsonException("机主信息不存在");
        }

        Message message = new Message();
        message.setIsFinish(Message.IsFinish.unFinish);
        message.setOrderNumber(order.getOrderNumber());
        message.setCategory(Message.Category.isclosed);
        message.setSendTime(new Date().getTime());
        message.setGuestPhone(guest.getPhone());//租客电话
        message.setAdminPhone(admin.getPhone());//机主电话
        baseDao.save(message);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void paymentTimeout(Order order) {
        // 更新状态为支付超时
        order.setState(Order.State.paymentTimeout);
        baseDao.update(order);
        // 添加订单处理详情
        OrderDetail detail = new OrderDetail();
        detail.setOrderNumber(order.getOrderNumber());
        detail.setTime(new Date().getTime());
        detail.setDescription("支付超时");
        detail.setUserId(order.getUserId());
        detail.setName(order.getName());
        baseDao.save(detail);

        // 订单支付之后设定--取机时间,还机时间通知
        SysmanUser guest = baseDao.getById(SysmanUser.class, order.getUserId());
        if (guest == null) {
            throw new JsonException("租客信息不存在");
        }
        SysmanUser admin = baseDao.getById(SysmanUser.class, order.getRentalUserId());
        if (admin == null) {
            throw new JsonException("机主信息不存在");
        }

        Message message = new Message();
        message.setIsFinish(Message.IsFinish.unFinish);
        message.setOrderNumber(order.getOrderNumber());
        message.setCategory(Message.Category.paymentTimeout);
        message.setSendTime(new Date().getTime());
        message.setGuestPhone(guest.getPhone());//租客电话
        message.setAdminPhone(admin.getPhone());//机主电话
        baseDao.save(message);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void reject(String orderNumber) {
        // 1.订单状态检查
        Order order = baseDao.getById(Order.class, orderNumber);
        if (order == null) {
            throw new JsonException("更新的订单不存在");
        }
        if (order.getState() != Order.State.ordered) {
            throw new JsonException("订单状态有误:" + order.getState());
        }

        order.setState(Order.State.reject);
        baseDao.update(order);

        // 1. 验证租客信息
        SysmanUser guest = baseDao.getById(SysmanUser.class, order.getUserId());
        if (guest == null) {
            throw new JsonException("租客信息不存在");
        }
        SysmanUser admin = baseDao.getById(SysmanUser.class, order.getRentalUserId());
        if (admin == null) {
            throw new JsonException("机主信息不存在");
        }

        // 2. 添加订单处理详情
        OrderDetail detail = new OrderDetail();
        detail.setOrderNumber(order.getOrderNumber());
        detail.setTime(new Date().getTime());
        detail.setDescription("机主拒绝接单,订单结束");
        detail.setUserId(order.getRentalUserId());
        detail.setName(order.getRentalName());
        baseDao.save(detail);

        // 3. 发送拒绝接单通知
        Message message = new Message();
        message.setIsFinish(Message.IsFinish.unFinish);
        message.setGuestPhone(guest.getPhone());//租客电话
        message.setAdminPhone(admin.getPhone());//机主电话
        message.setOrderNumber(order.getOrderNumber());
        message.setCategory(Message.Category.reject);//拒绝接单短信
        message.setSendTime(new Date().getTime());//立刻发送
        message.setTemplateId("未知");
        message.setParameters("发送拒绝接单通知");
        baseDao.save(message);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void cancel(String orderNumber) {
        // 1. 订单状态检查
        Order order = baseDao.getById(Order.class, orderNumber);
        if (order == null) {
            throw new JsonException("更新的订单不存在");
        }
        if (order.getState() != Order.State.accepted && order.getState() != Order.State.ordered) {
            throw new JsonException("该订单不能被取消:" + order.getState());
        }
        order.setState(Order.State.userCanceled);
        baseDao.update(order);

        // 2. 添加订单处理详情
        OrderDetail detail = new OrderDetail();
        detail.setOrderNumber(order.getOrderNumber());
        detail.setTime(new Date().getTime());
        detail.setDescription("租客取消订单,订单结束");
        detail.setUserId(order.getUserId());
        detail.setName(order.getName());
        baseDao.save(detail);

        // 3.取消订单通知处理
        SysmanUser guest = baseDao.getById(SysmanUser.class, order.getUserId());
        if (guest == null) {
            throw new JsonException("租客信息不存在");
        }
        SysmanUser admin = baseDao.getById(SysmanUser.class, order.getRentalUserId());
        if (admin == null) {
            throw new JsonException("机主信息不存在");
        }

        Message message = new Message();
        message.setIsFinish(Message.IsFinish.unFinish);
        message.setOrderNumber(order.getOrderNumber());
        message.setCategory(Message.Category.userCanceled);
        message.setSendTime(new Date().getTime());
        message.setGuestPhone(guest.getPhone());//租客电话
        message.setAdminPhone(admin.getPhone());//机主电话
        message.setParameters("租客取消订单");
        baseDao.save(message);
    }

    /**
     * 提交订单争议信息
     *
     * @param order
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void hasDispute(Order order) {
        // 1. 更新订单信息
        order.setState(Order.State.hasDispute);
        baseDao.update(order);

        // 2. 添加订单处理详情
        OrderDetail detail = new OrderDetail();
        detail.setOrderNumber(order.getOrderNumber());
        detail.setTime(new Date().getTime());
        detail.setDescription("机主提交争议处理请求,进入理赔流程");
        detail.setUserId(order.getRentalUserId());
        detail.setName(order.getRentalName());
        baseDao.save(detail);

        // 3. 添加消息处理信息
        SysmanUser guest = baseDao.getById(SysmanUser.class, order.getUserId());
        if (guest == null) {
            throw new JsonException("租客信息不存在");
        }
        SysmanUser admin = baseDao.getById(SysmanUser.class, order.getRentalUserId());
        if (admin == null) {
            throw new JsonException("机主信息不存在");
        }
        /**订单有争议*/
        /**系统收到提交的证据材料后，短信提醒双方理赔申请已收到，工作人员会尽快与双方联系进一步处理*/
        Message message = new Message();
        message.setIsFinish(Message.IsFinish.unFinish);
        message.setOrderNumber(order.getOrderNumber());
        message.setCategory(Message.Category.hasDispute);
        message.setSendTime(new Date().getTime());//
        message.setGuestPhone(guest.getPhone());//租客电话
        message.setAdminPhone(admin.getPhone());//机主电话
        message.setTemplateId("id");
        message.setParameters("订单有争议,进入争议处理流程");
        baseDao.save(message);
    }

    /**
     * 解决争议,将钱退给机主和租客
     * 退还租客金额    disputeRefund;
     * 退还机主金额  disputeRefundRental;
     *
     * @param order
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void handleDispute(Order order) {
        SysmanUser guest = baseDao.getById(SysmanUser.class, order.getUserId());
        if (guest == null) {
            throw new JsonException("租客信息不存在");
        }
        SysmanUser admin = baseDao.getById(SysmanUser.class, order.getRentalUserId());
        if (admin == null) {
            throw new JsonException("机主信息不存在");
        }
        // 查询原始订单信息
        Order temp = baseDao.getById(Order.class, order.getOrderNumber());
        if (temp == null) {
            throw new JsonException("处理的订单不存在");
        }
        if (temp.getState() != Order.State.hasDispute) {
            throw new JsonException("该订单不存在争议");
        }
        // 退还的钱不能超过支付的钱
        if (order.getDisputeRefund() + order.getDisputeRefundRental() > temp.getPayAmount() + temp.getCashAmount()) {
            throw new JsonException("退款金额不能超过订单总金额");
        }

        // 更新状态为依据退款完成
        temp.setRefund(order.getDisputeRefund());// 退还租客的钱
        temp.setRefundRental(order.getDisputeRefundRental()); //退还机主的钱
        // 如果没有退还给租客金额直接返回
        if (order.getDisputeRefund() <= 0.0001) {// 如果没有退给租客钱,那么订单直接退款完成
            temp.setState(Order.State.refunded);
        } else {
            temp.setState(Order.State.disputeFinish);
        }
        baseDao.update(temp);

        // 更新退还给机主的信息 amount 退给机主的钱
        admin.setAmount(admin.getAmount() + order.getDisputeRefundRental());
        baseDao.update(admin);

        // 解决争议通知
        Message message = new Message();
        message.setIsFinish(Message.IsFinish.unFinish);
        message.setOrderNumber(order.getOrderNumber());
        if (order.getDisputeRefund() <= 0.0001) {// 如果没有退给租客钱,那么订单直接退款完成
            message.setCategory(Message.Category.refunded);
        } else {
            message.setCategory(Message.Category.disputeFinish);
        }
        message.setSendTime(new Date().getTime());//
        message.setGuestPhone(guest.getPhone());//租客电话
        message.setAdminPhone(admin.getPhone());//机主电话
        message.setTemplateId("id");
        message.setParameters("处理订单争议");
        baseDao.save(message);

        // 订单处理详情
        OrderDetail detail = new OrderDetail();
        detail.setOrderNumber(order.getOrderNumber());
        detail.setTime(new Date().getTime());
        detail.setDescription("争议订单,等待退款回调,退款金额:机主: " + order.getDisputeRefundRental() + " 租客: " + order.getDisputeRefund());
        detail.setName("系统管理员");
        baseDao.save(detail);

        // 如果没有退还给租客金额直接返回
        if (order.getDisputeRefund() <= 0.0001) {
            return;
        }

        // 争议订单退款
        RefundRequest refundRequest = WSHelper.getInitRefundRequest();
        refundRequest.setMemo("异常订单退款");//备注
        refundRequest.setMerchantNo(Constant.merchantNo);//商户号
        refundRequest.setNotifyURL(Constant.refundNotifyUrl);//回调URL
        refundRequest.setOriginalOrderNo(temp.getOrderNumber());//原始订单号
        refundRequest.setRefundAmount(new DecimalFormat("#.00").format(order.getDisputeRefund()));//退款金额 退给租客的钱
        refundRequest.setRefundOrderNo("" + new Date().getTime());//退款订单号

        WSProxy proxy = new WSProxy();
        try {
            // 签名
            WSHelper.signRefundRequest(refundRequest, Constant.merchantKey);
            RefundResponse response = proxy.doRefund(refundRequest);
            if (response.getStatus() == null || response.getStatus().equals("02")) {// 00:处理中;01：成功;02:失败;
                throw new JsonException(response.getReturnInfo() == null ? "未知错误" : response.getReturnInfo().getErrorMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new JsonException("退款失败:" + e.getMessage());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Pagination<Order> query(Order order, int currentPage, int pageSize) {
        List<QueryCondition> conditions = new ArrayList<>();
        // 查询未删除的
        if (order != null) {
            if (order.getDeleted() == 0) {//是否已经删除
                conditions.add(new QueryCondition("deleted", QueryCondition.LE, Order.DeleteState.unDelete));
            } else {
                conditions.add(new QueryCondition("deleted", QueryCondition.EQ, order.getDeleted()));
            }
            if (order.getUserId() != 0) {//用户ID
                conditions.add(new QueryCondition("userId", QueryCondition.EQ, order.getUserId()));
            }
            if (order.getRentalUserId() != 0) {//机主ID
                conditions.add(new QueryCondition("rentalUserId", QueryCondition.EQ, order.getRentalUserId()));
            }
            if (order.getRentalId() != 0) {//租赁信息ID
                conditions.add(new QueryCondition("rentalId", QueryCondition.EQ, order.getRentalId()));
            }
            if (!ValidateUtil.isEmpty(order.getOrderNumber())) {//订单编号
                conditions.add(new QueryCondition("orderNumber", QueryCondition.LK, order.getOrderNumber()));
            }
            if (order.getState() != 0) {//订单状态
                conditions.add(new QueryCondition("state", QueryCondition.EQ, order.getState()));
            }
            if (!ValidateUtil.isEmpty(order.getName())) {//租客姓名
                conditions.add(new QueryCondition("name", QueryCondition.LK, order.getName()));
            }
            if (!ValidateUtil.isEmpty(order.getNickName())) {//租客昵称
                conditions.add(new QueryCondition("nickName", QueryCondition.LK, order.getNickName()));
            }
            if (!ValidateUtil.isEmpty(order.getRentalName())) {//机主姓名
                conditions.add(new QueryCondition("rentalName", QueryCondition.LK, order.getRentalName()));
            }
            if (!ValidateUtil.isEmpty(order.getRentalNickName())) {//机主昵称
                conditions.add(new QueryCondition("rentalNickName", QueryCondition.LK, order.getRentalNickName()));
            }
            if (!ValidateUtil.isEmpty(order.getAddress())) {//地址
                conditions.add(new QueryCondition("address", QueryCondition.LK, order.getAddress()));
            }
        }
        return getPagination(Order.class, conditions, " order by createTime desc ", currentPage, pageSize);
    }

    /**
     * 更新时间
     *
     * @param orderNumber   订单编号
     * @param newObtainTime 新的取机时间
     * @param newReturnTime 新的还机时间
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public JsonResponse updateTime(String orderNumber, long newObtainTime, long newReturnTime) {
        // 查询订单
        Order temp = baseDao.getById(Order.class, orderNumber);
        if (temp == null) {
            throw new JsonIOException("订单不存在");
        }
        if (temp.getNewObtainTime() > 0 && newObtainTime > 0) {
            throw new JsonIOException("取机时间已经被更新过不允许再修改");
        }
        if (temp.getNewReturnTime() > 0 && newReturnTime > 0) {
            throw new JsonIOException("还机时间已经被更新过不允许再修改");
        }

        // 取机时间 如果传了新的取机时间,且未修改过
        if (newObtainTime > 0 && temp.getNewObtainTime() <= 0) {
            temp.setNewObtainTime(newObtainTime);
        }
        if (newReturnTime > 0 & temp.getNewReturnTime() <= 0) {
            temp.setNewReturnTime(newReturnTime);
        }

        //真实的取机时间,拿相机的时间,取最新值
        long obtTime = temp.getNewObtainTime() > 0 ? temp.getNewObtainTime() : temp.getObtainTime();
        //真实的还机时间,还相机的时间,取最新值
        long retTime = temp.getNewReturnTime() > 0 ? temp.getNewReturnTime() : temp.getReturnTime();
        if (obtTime > retTime) {//如果取机时间在还机时间之后
            throw new JsonIOException("取机时间不能在还机时间之后");
        }

        // 6. 查询该租赁信息正在出租的信息,验证该时间段是否允许租机
        List<QueryCondition> conditions = new ArrayList<>();
        conditions.add(new QueryCondition("rentalId", QueryCondition.EQ, temp.getRentalId()));//租赁信息ID
        conditions.add(new QueryCondition("state", QueryCondition.LT, Order.State.waitingRefundCallBack));//未完成的订单
        conditions.add(new QueryCondition("orderNumber", QueryCondition.NEQ, orderNumber));//非当前订单
        List<Order> orders = get(Order.class, conditions);

        // 这次下单的时间区间
        long start = ValidateUtil.dayOfStar(obtTime);
        long end = ValidateUtil.dayOfEnd(retTime);
        if (orders != null && orders.size() > 0) {
            for (Order order1 : orders) {
                // 真实的
                long hasObtainTime = order1.getRealObtainTime();
                // 真实的
                long hasReturnTime = order1.getRealReturnTime();
                // 如果新的订单的 取机时间 在已有订单的 取机时间 和还机时间 区间内
                //或者 如果新的订单的 还机时间 在已有订单的 取机时间 和还机时间 区间内
                if (start < hasObtainTime && hasObtainTime < end || start < hasReturnTime && hasReturnTime < end) {
                    throw new JsonIOException("您选择的时间段,该相机正在出租");
                }
            }
        }

        // 租赁天数计算取最小取机日期
        long startDay = temp.getNewObtainTime() > 0 ? Math.min(temp.getObtainTime(), temp.getNewObtainTime()) : temp.getObtainTime();
        long endDay = Math.max(temp.getReturnTime(), temp.getNewReturnTime());
        // 4. 计算租赁天数
        int d = Math.max(calculateRentalDays(startDay, endDay), temp.getRentalDays());
        temp.setNewRentalDays(d);
        // 计算租金
        // 1. 检查租赁信息是否存在
        RentalInfo rentalInfo = baseDao.getById(RentalInfo.class, temp.getRentalId());
        if (rentalInfo == null) {
            throw new JsonIOException("租赁信息不存在");
        }
        double rentalAmount = calculateRental(d, rentalInfo.getPrice());//租金(不含保险)
        double insurance = (rentalAmount * 0.05);//强制保险
        double deductibleInsurance = 0;//免赔险
        if (temp.getDeductibleInsurance() > 0) {//如果选择了免赔险
            deductibleInsurance = (rentalAmount * 0.15);
        }
        // 将各种金额写入订单
        temp.setNewInsurance(insurance);//强制保险
        temp.setNewDeductibleInsurance(deductibleInsurance);//免赔险
        temp.setNewRentalAmount(rentalAmount);//租金(不含保险)
        temp.setNewAmount(rentalAmount + insurance + deductibleInsurance + rentalInfo.getDeposit());//订单总金额=押金+租金+强制保险+免赔险
        //temp.setNewRefund(0);// 退款
        baseDao.update(temp);

        // 添加订单处理详情
        OrderDetail detail = new OrderDetail();
        detail.setOrderNumber(temp.getOrderNumber());
        detail.setTime(new Date().getTime());
        detail.setDescription("租客更新取机时间或还机时间");
        detail.setUserId(temp.getRentalUserId());
        detail.setName(temp.getRentalName());
        baseDao.save(detail);

        JsonResponse response = JsonResponse.success("时间更新成功");
        response.setData("order", temp);
        return response;
    }

    /**
     * 添加消息
     *
     * @param message
     */
    private void addMessage(Message message) {
        if (message == null) throw new JsonException("消息内容不能为空");
        if (ValidateUtil.isEmpty(message.getOrderNumber())) throw new JsonException("订单编号不能为空");
        if (message.getCategory() == Message.Category.nullVal) throw new JsonException("消息类型不能为空");
        switch (message.getCategory()) {
            case Message.Category.checkOrderAccept: {//订单接受超时检测
                Calendar calendar = Calendar.getInstance(Locale.getDefault());
                calendar.setTime(new Date());
                calendar.add(Calendar.MINUTE, 15);//接受订单十五分钟超时
                message.setSendTime(calendar.getTime().getTime());
                baseDao.save(message);
                break;
            }
            case Message.Category.checkOrderPay: {//订单支付
                Calendar calendar = Calendar.getInstance(Locale.getDefault());
                calendar.setTime(new Date());
                calendar.add(Calendar.MINUTE, 45);//接受订单四十五分钟超时
                message.setSendTime(calendar.getTime().getTime());
                baseDao.save(message);
                break;
            }
            case Message.Category.pushObtain: {//取机消息推送,短信等等
                if (message.getSendTime() == 0) throw new JsonException("消息处理时间不能为空");
                if (ValidateUtil.isEmpty(message.getGuestPhone())) throw new JsonException("手机号码不能为空");
                if (ValidateUtil.isEmpty(message.getTemplateId())) throw new JsonException("短信模板不能为空");
                if (ValidateUtil.isEmpty(message.getParameters())) throw new JsonException("模板参数不能为空");
                baseDao.save(message);
                break;
            }
            case Message.Category.pushReturn: {//还机消息推送,短信等等
                if (message.getSendTime() == 0) throw new JsonException("消息处理时间不能为空");
                if (ValidateUtil.isEmpty(message.getGuestPhone())) throw new JsonException("手机号码不能为空");
                if (ValidateUtil.isEmpty(message.getTemplateId())) throw new JsonException("短信模板不能为空");
                if (ValidateUtil.isEmpty(message.getParameters())) throw new JsonException("模板参数不能为空");
                baseDao.save(message);
                break;
            }
        }
    }

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssS");
    private static Random r = new Random();

    /**
     * 生成订单编号
     *
     * @return
     */
    private static String generateOrderNumber() {
        StringBuffer code = new StringBuffer(dateFormat.format(new Date()));
        int i = 0;
        while (i < 4) {
            int t = r.nextInt(10);
            code.append(t);
            i++;
        }
        return code.toString();
    }

    /**
     * 计算租金
     *
     * @param rentalDays 租赁天数
     * @param rental     第一天的租金
     * @return 总的租金
     */
    public static double calculateRental(double rentalDays, double rental) {
        if (rentalDays <= 0 || rental <= 0) return 0;
        double result = rental;//第一天
        rentalDays = rentalDays - 1;
        if (rentalDays > 0) {//第二天开始按第一天的25%
            result += rentalDays * rental * 0.25;
        }
        return result;
    }

    /**
     * 计算租赁天数
     *
     * @param obtainTime 取机时间
     * @param returnTime 还机时间
     * @return 租赁天数
     */
    public static int calculateRentalDays(long obtainTime, long returnTime) {
        long d = returnTime - obtainTime;
        int t = (int) Math.round((double) Math.abs(d) / (double) (24 * 60 * 60 * 1000));
        if (t <= 0) t = 1;
        return t;
    }
}
