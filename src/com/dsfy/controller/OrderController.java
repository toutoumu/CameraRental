package com.dsfy.controller;

import com.dsfy.dao.util.Pagination;
import com.dsfy.dao.util.QueryCondition;
import com.dsfy.entity.ImageInfo;
import com.dsfy.entity.RentalInfo;
import com.dsfy.entity.http.JsonResponse;
import com.dsfy.entity.pay.*;
import com.dsfy.refund.ws.WSHelper;
import com.dsfy.refund.ws.WSProxy;
import com.dsfy.service.*;
import com.dsfy.util.Constant;
import com.dsfy.util.UploadUtil;
import com.dsfy.util.ValidateUtil;
import com.sdo.mas.common.api.query.order.entity.syn.single.OrderQueryRequest;
import com.sdo.mas.common.api.query.order.entity.syn.single.OrderQueryResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 订单管理
 *
 * @author dell
 */
@Controller
@RequestMapping(value = "/OrderController", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
public class OrderController {
    @Resource(name = "OrderService")
    private IOrderService orderService;
    @Resource(name = "OrderDetailService")
    private IOrderDetailService detailService;
    @Resource(name = "CashCouponService")
    private ICashCouponService cashCouponService;
    @Resource(name = "RentalService")
    private IRentalService rentalService;
    @Resource(name = "RejectTimeService")
    private IRejectTimeService rejectTimeService;
    @Resource(name = "ImageService")
    private IImageService imageService;

    /**
     * 下单</br>
     * 1. 保存订单记录到OrderDetail
     * 2. 填写订单默认数值
     * 3. 发送短信通知机主
     * 4. 如果默认自动接单...
     *
     * @param order
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/order.do")
    public JsonResponse order(@RequestBody(required = false) Order order) {
        // 1. 请求参数验证
        if (order == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (order.getRentalId() == 0) {
            return JsonResponse.error("租赁信息ID不能为空");
        }
        if (order.getUserId() == 0) {
            return JsonResponse.error("用户ID不能为空");
        }
        if (order.getObtainTime() <= 0) {
            return JsonResponse.error("取机时间不能为空");
        }
        if (order.getReturnTime() <= 0) {
            return JsonResponse.error("还机时间不能为空");
        }
        if (order.getObtainTime() > order.getReturnTime()) {
            return JsonResponse.error("取机时间不能在还机时间之后");
        }

        // 2. 如果机主设置了该时间段不允许出租
        if (rejectTimeService.hasRejectTimeInRange(order.getRentalUserId(), order.getRentalId(), order.getObtainTime(), order.getReturnTime()) > 0) {
            return JsonResponse.error("您选择的时间段,机主不希望出租相机");
        }

        // 3. 保存订单信息
        return orderService.order(order);
    }

    /**
     * 接受订单
     *
     * @param order
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/accept.do")
    public JsonResponse accept(@RequestBody(required = false) Order order) {
        // 1. 请求参数验证
        if (order == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (ValidateUtil.isEmpty(order.getOrderNumber())) {
            return JsonResponse.error("主键不能为空");
        }

        // 2. 检验订单状态
        Order temp = orderService.getById(Order.class, order.getOrderNumber());
        if (temp == null) {
            return JsonResponse.error("更新的订单不存在");
        }
        if (temp.getState() == Order.State.acceptTimeout) {//订单接受超时
            return JsonResponse.error("订单已经超时");
        }
        if (temp.getState() != Order.State.ordered) {//订单已经受
            return JsonResponse.error("您已经成功接收了此订单,请勿重复处理");
        }

        temp.setState(Order.State.accepted);
        orderService.accept(temp);
        return JsonResponse.success("您已成功接受订单");
    }

    /**
     * 客户端支之前调用，代金券信息更新上去，计算出需要支付的现金
     * 这个接口将处理代金券
     *
     * @param order
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/payment.do")
    public JsonResponse payment(@RequestBody(required = false) Order order) {
        if (order == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (ValidateUtil.isEmpty(order.getOrderNumber())) {
            return JsonResponse.error("主键不能为空");
        }
        orderService.payment(order.getOrderNumber(), order.getCashId());
        return JsonResponse.success("更新成功");
    }

    /**
     * 第三方支付回调接口
     *
     * @param message
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/paymentCallBack.do")
    public void paymentCallBack(NotifyMessage message, HttpServletResponse response, HttpServletRequest request) throws IOException {
        // Constant.RequestToFile(request, "支付回调.text");//将响应写入文件
        orderService.add(message);//保存回调信息
        if (ValidateUtil.isEmpty(message.getOrderNo())) {
            response.getWriter().write("ERROR");
            return;
        }
        orderService.paymentCallBack(message.getOrderNo());
        response.getWriter().write("OK");
    }

    /**
     * 机主交机相机
     *
     * @param order
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/obtainedNotice.do")
    public JsonResponse obtainedNotice(@RequestBody(required = false) Order order) {
        // 1. 请求参数检测
        if (order == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (ValidateUtil.isEmpty(order.getOrderNumber())) {
            return JsonResponse.error("主键不能为空");
        }

        // 2. 订单状态检测
        Order temp = orderService.getById(Order.class, order.getOrderNumber());
        if (temp == null) {
            return JsonResponse.error("更新的订单不存在");
        }
        if (temp.getState() == Order.State.obtainedNotice) {//如果已经给出了
            return JsonResponse.error("请勿重复执行交出相机操作:" + temp.getState());
        }
        if (temp.getState() < Order.State.paymented) {//如果未支付
            return JsonResponse.error("订单未支付,不能交出相机:" + temp.getState());
        }
        if (temp.getState() != Order.State.paymented) {
            return JsonResponse.error("订单状态不正确,不能交出相机:" + temp.getState());
        }

        // 3. 更新相关信息
        temp.setState(Order.State.obtainedNotice);
        orderService.obtainedNotice(temp);
        return JsonResponse.success("交出相机成功");
    }

    /**
     * 租客取机
     *
     * @param order
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/obtained.do")
    public JsonResponse obtained(@RequestBody(required = false) Order order) {
        // 1. 请求参数检测
        if (order == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (ValidateUtil.isEmpty(order.getOrderNumber())) {
            return JsonResponse.error("主键不能为空");
        }

        // 2. 订单状态检测
        Order temp = orderService.getById(Order.class, order.getOrderNumber());
        if (temp == null) {
            return JsonResponse.error("更新的订单不存在");
        }
        if (temp.getState() == Order.State.obtained) {//如果已经取机
            return JsonResponse.error("请勿重复执行取机操作:" + temp.getState());
        }
        if (temp.getState() < Order.State.obtainedNotice) {
            return JsonResponse.error("机主还未通知取机,不能执行确认取机操作:" + temp.getState());
        }
        if (temp.getState() != Order.State.obtainedNotice) {
            return JsonResponse.error("订单状态有误:" + temp.getState());
        }

        // 3. 更新相关信息
        temp.setState(Order.State.obtained);
        orderService.obtained(temp);
        return JsonResponse.success("操作成功");
    }

    /**
     * 租客还机
     *
     * @param order
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/returnedNotice.do")
    public JsonResponse returnedNotice(@RequestBody(required = false) Order order) {
        // 1. 请求参数检测
        if (order == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (ValidateUtil.isEmpty(order.getOrderNumber())) {
            return JsonResponse.error("主键不能为空");
        }

        // 2. 订单状态检测
        Order temp = orderService.getById(Order.class, order.getOrderNumber());
        if (temp == null) {
            return JsonResponse.error("更新的订单不存在");
        }
        if (temp.getState() == Order.State.returnedNotice) {//如果已经通知还机
            return JsonResponse.error("请勿重复执行还机操作:" + temp.getState());
        }
        if (temp.getState() < Order.State.obtained) {
            return JsonResponse.error("租客还未拿到相机,不能执行还机操作:" + temp.getState());
        }
        if (temp.getState() != Order.State.obtained) {
            return JsonResponse.error("订单状态有误:" + temp.getState());
        }

        // 3. 更新相关信息
        temp.setState(Order.State.returnedNotice);
        orderService.returnedNotice(temp);
        return JsonResponse.success("操作成功");
    }

    /**
     * 机主确认还机
     *
     * @param order
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/returned.do")
    public JsonResponse returned(@RequestBody(required = false) Order order) {
        // 1. 请求参数检测
        if (order == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (ValidateUtil.isEmpty(order.getOrderNumber())) {
            return JsonResponse.error("主键不能为空");
        }

        // 2. 订单状态检测
        Order temp = orderService.getById(Order.class, order.getOrderNumber());
        if (temp == null) {
            return JsonResponse.error("更新的订单不存在");
        }
        if (temp.getState() < Order.State.returnedNotice) {
            return JsonResponse.error("租客还未申请还机,不能执行确认还机操作:" + temp.getState());
        }
        if (temp.getState() != Order.State.returnedNotice) {
            return JsonResponse.error("订单状态有误:" + temp.getState());
        }

        // 3. 更新相关信息
        temp.setState(Order.State.returned);
        orderService.returned(temp);
        return JsonResponse.success("操作成功");
    }

    /**
     * 后台发起退款接口
     * 1.验证退款金额
     * 2.更新相关数据
     *
     * @param order
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/refund.do")
    public JsonResponse refund(@RequestBody(required = false) Order order) {
        // 1. 请求参数检测
        if (order == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (ValidateUtil.isEmpty(order.getOrderNumber())) {
            return JsonResponse.error("主键不能为空");
        }

        orderService.refund(order.getOrderNumber());
        return JsonResponse.success("操作成功");
    }

    /**
     * 退款完成回调
     *
     * @param order
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/refundCallBack.do")
    public void refundCallBack(RefundNotifyMessage order, HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Constant.RequestToFile(request, "退款回调");//保存回调请求数据
        orderService.add(order);//保存回调信息到数据库
        if (order == null) {
            response.getWriter().write("请求参数不能为空");
        }
        if (ValidateUtil.isEmpty(order.getOriginalOrderNo())) {
            response.getWriter().write("ERROR");
        }

        orderService.refundCallback(order.getOriginalOrderNo());
        response.getWriter().write("OK");
    }

    /**
     * 用户已经评论(订单关闭)此方法没有被调用
     *
     * @param order
     * @return
     */
    @Deprecated
    @ResponseBody
    @RequestMapping(value = "/close.do")
    public JsonResponse close(@RequestBody(required = false) Order order) {
        if (order == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (ValidateUtil.isEmpty(order.getOrderNumber())) {
            return JsonResponse.error("主键不能为空");
        }
        Order temp = orderService.getById(Order.class, order.getOrderNumber());
        if (temp == null) {
            return JsonResponse.error("更新的订单不存在");
        }
        if (temp.getState() != Order.State.refunded) {
            return JsonResponse.error("退款之后才能评论");
        }
        temp.setState(Order.State.isclosed);
        orderService.close(temp);
        return JsonResponse.success("操作成功");
    }

    /**
     * 拒绝订单
     *
     * @param order
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/reject.do")
    public JsonResponse reject(@RequestBody(required = false) Order order) {
        // 1. 输入参数验证
        if (order == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (ValidateUtil.isEmpty(order.getOrderNumber())) {
            return JsonResponse.error("主键不能为空");
        }

        orderService.reject(order.getOrderNumber());
        return JsonResponse.success("操作成功");
    }

    /**
     * 用户取消订单
     *
     * @param order
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/cancel.do")
    public JsonResponse cancel(@RequestBody(required = false) Order order) {
        // 1. 监测订单状态
        if (order == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (ValidateUtil.isEmpty(order.getOrderNumber())) {
            return JsonResponse.error("主键不能为空");
        }

        orderService.cancel(order.getOrderNumber());
        return JsonResponse.success("操作成功");
    }

    /**
     * 租客支付超时(取消订单),这个方法不会被调用
     *
     * @param order
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/paymentTimeout.do")
    public JsonResponse paymentTimeout(@RequestBody(required = false) Order order) {
        if (order == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (ValidateUtil.isEmpty(order.getOrderNumber())) {
            return JsonResponse.error("主键不能为空");
        }
        Order temp = orderService.getById(Order.class, order.getOrderNumber());
        if (temp == null) {
            return JsonResponse.error("更新的订单不存在");
        }
        if (temp.getState() != Order.State.accepted) {
            return JsonResponse.error("订单状态有误");
        }
        temp.setState(Order.State.paymentTimeout);
        orderService.update(temp);
        return JsonResponse.success("操作成功");
    }

    /**
     * 进入理赔流程(订单有争议)
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/hasDispute.do")
    public JsonResponse hasDispute(@RequestParam(value = "images", required = false) MultipartFile[] images,
                                   @RequestParam(value = "orderNumber", required = false) String orderNumber,
                                   @RequestParam(value = "reason", required = false) String reason) throws IOException {
        // 1. 输入参数检查 (订单编号,争议理由,争议照片)
        if (ValidateUtil.isEmpty(orderNumber)) {
            return JsonResponse.error("订单编号不能为空");
        }
        if (ValidateUtil.isEmpty(reason)) {
            return JsonResponse.error("争议描述不能为空");
        }
        if (images == null || images.length == 0) {
            return JsonResponse.error("上传文件不能为空");
        }
        int i = 0;
        for (MultipartFile image : images) {
            if (!image.isEmpty()) {
                i++;
                break;
            }
        }
        if (i == 0) {
            return JsonResponse.error("上传文件不能为空");
        }

        // 2. 检测订单是否存在
        Order order = orderService.getById(Order.class, orderNumber);
        if (order == null) {
            return JsonResponse.error("订单信息不存在");
        }
        // 3. 只有租客通知还机,和租客已经确认还机的订单才能进入争议处理流程
        if (order.getState() != Order.State.returnedNotice && order.getState() != Order.State.returned) {
            return JsonResponse.error("订单状态有误");
        }
        // 4. 将争议图片保存
        List<ImageInfo> imageInfos = new ArrayList<>();
        for (MultipartFile image : images) {
            if (image.isEmpty()) continue;
            UploadUtil.saveFile(image, imageInfos);
        }
        // 这个是必须的要不然查不到
        List<ImageInfo> demos = new ArrayList<>();
        for (ImageInfo info : imageInfos) {
            info.setCategory(ImageInfo.Category.disputeImage);//图片类型
            info.setForeignKey(orderNumber);//外键
            if (info.getSize() == ImageInfo.Size.small) {
                demos.add(info);
            }
        }
        imageService.batchSave(imageInfos);

        // 组织返回数据
        order.setDemos(demos);//争议图片
        order.setState(Order.State.hasDispute);// 有争议
        order.setReason(reason); //争议原因
        orderService.hasDispute(order);

        JsonResponse response = JsonResponse.success("申请纠纷处理成功");
        response.setData("order", order);
        return response;
    }

    /**
     * 解决争议,将钱退给机主和租客
     * 退还租客金额    disputeRefund;
     * 退还机主金额  disputeRefundRental;
     *
     * @param order
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/handleDispute.do")
    public JsonResponse handleDispute(@RequestBody(required = false) Order order) {
        if (order == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (ValidateUtil.isEmpty(order.getOrderNumber())) {
            return JsonResponse.error("主键不能为空");
        }
        if (order.getUserId() == 0) {
            return JsonResponse.error("用户ID不能为空");
        }
        if (order.getRentalUserId() == 0) {
            return JsonResponse.error("机主ID不能为空");
        }
        orderService.handleDispute(order);
        return JsonResponse.success("退款完成");
    }
    /*******************************************************************************/

    /**
     * 订单相机交接时候的图片
     *
     * @param images
     * @param orderNumber
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/uploadDemo.do")
    public JsonResponse uploadDemo(//
                                   @RequestParam(value = "images", required = false) MultipartFile[] images,
                                   @RequestParam(value = "orderNumber", required = false, defaultValue = "") String orderNumber
    ) throws IOException {
        if (images == null || images.length == 0) {
            return JsonResponse.error("上传文件不能为空");
        }
        int count = 0;
        for (MultipartFile image : images) {
            if (image.isEmpty()) continue;
            count++;
        }
        if (count == 0) {
            return JsonResponse.error("上传文件不能为空");
        }
        if (ValidateUtil.isEmpty(orderNumber)) {
            return JsonResponse.error("订单编号不能为空");
        }
        // 检测订单是否存在
        Order order = orderService.getById(Order.class, orderNumber);
        if (order == null) {
            return JsonResponse.error("订单信息不存在");
        }
        // 查询已有样图
        List<ImageInfo> demos = imageService.query(ImageInfo.Category.orderImage, "" + orderNumber);
        count = 0;
        if (demos != null) count = demos.size();
        List<ImageInfo> imageInfos = new ArrayList<>();
        for (MultipartFile image : images) {
            if (image.isEmpty()) continue;
            UploadUtil.saveFile(image, imageInfos);
            count++;
            //保存图片做多10张
            //if (count == 10) break;
        }
        // 这个是必须的要不然查不到
        for (ImageInfo info : imageInfos) {
            info.setCategory(ImageInfo.Category.orderImage);
            info.setForeignKey("" + orderNumber);
            if (info.getSize() == ImageInfo.Size.small) {
                demos.add(info);
            }
        }
        imageService.batchSave(imageInfos);
        // 组织返回数据
        order.setDemos(demos);
        JsonResponse response = JsonResponse.success("上传成功");
        response.setData("order", order);
        return response;
    }

    /**
     * 删除订单样张
     *
     * @param image
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteDemo.do")
    public JsonResponse deleteDemo(@RequestBody(required = false) ImageInfo image) {
        if (image == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (image.getCategory() != ImageInfo.Category.orderImage) {
            return JsonResponse.error("删除的照片的类型不正确");
        }
        if (ValidateUtil.isEmpty(image.getForeignKey()) || "0".equals(image.getForeignKey())) {
            return JsonResponse.error("主键不能为空");
        }
        if (image.getImageId() == 0) {
            return JsonResponse.error("主键不能为空");
        }
        //删除图片
        imageService.delete(ImageInfo.class, image.getImageId());
        // 组织返回数据
        return JsonResponse.success("删除成功");
    }

    /**
     * 根据条件查询订单信息 (默认查询未删除的,用户ID,租赁信息ID,订单编号,订单状态,租客姓名,租客昵称,机主姓名,机主昵称)
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/query.do")
    public JsonResponse query(@RequestBody(required = false) Pagination<Order> pagination) {
        if (pagination == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (pagination.getData() == null) {
            pagination.setData(new Order());
        }
        // 分页查询
        pagination = orderService.query(pagination.getData(), pagination.getCurrentPage(), pagination.getPageSize());
        JsonResponse response = JsonResponse.success("查询成功");
        response.setData("pagination", pagination);
        return response;
    }

    /**
     * 设置订单为删除状态
     *
     * @param order
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delete.do")
    public JsonResponse delete(@RequestBody(required = false) Order order) {
        if (order == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (ValidateUtil.isEmpty(order.getOrderNumber())) {
            return JsonResponse.error("订单编号不能为空");
        }

        // 查询订单
        order = orderService.getById(Order.class, order.getOrderNumber());
        if (order == null) {
            return JsonResponse.error("删除的订单不存在");
        }
        if (order.getState() <= Order.State.isclosed) {
            return JsonResponse.error("订单未完成不能删除");
        }
        order.setDeleted(Order.DeleteState.deleted);
        orderService.update(order);
        return JsonResponse.success("删除成功");
    }

    /**
     * 根据主键查询订单
     *
     * @param order
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getById.do")
    public JsonResponse getById(@RequestBody(required = false) Order order) {
        if (order == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (ValidateUtil.isEmpty(order.getOrderNumber())) {
            return JsonResponse.error("主键不能为空");
        }
        // 查询订单
        order = orderService.getById(Order.class, order.getOrderNumber());
        if (order == null) {
            return JsonResponse.error("订单不存在");
        }
        //查询相关的租赁信息
        RentalInfo rentalInfo = orderService.getById(RentalInfo.class, order.getRentalId());
        order.setRentalInfo(rentalInfo);
        // 查询订单图片 //图片类型(样张)
        List<ImageInfo> demos = imageService.query(ImageInfo.Category.orderImage, order.getOrderNumber());
        order.setDemos(demos);
        // 查询争议图片 //图片类型(有争议)
        List<ImageInfo> dis = imageService.query(ImageInfo.Category.disputeImage, order.getOrderNumber());
        order.setDisputeImage(dis);
        // 查询订单详情
        List<OrderDetail> details = detailService.get(OrderDetail.class, new QueryCondition("orderNumber", QueryCondition.EQ, order.getOrderNumber()));
        order.setDetails(details);
        // 查询优惠券
        List<CashCoupon> cashCoupons = cashCouponService.get(CashCoupon.class, new QueryCondition("orderNumber", QueryCondition.EQ, order.getOrderNumber()));
        order.setCashCoupons(cashCoupons);
        JsonResponse response = JsonResponse.success("操作成功");
        response.setData("order", order);
        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/update.do")
    public JsonResponse update(@RequestBody(required = false) Order order) {
        if (order == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (ValidateUtil.isEmpty(order.getOrderNumber())) {
            return JsonResponse.error("主键不能为空");
        }
        // 查询订单
        Order temp = orderService.getById(Order.class, order.getOrderNumber());
        if (temp == null) {
            return JsonResponse.error("订单不存在");
        }
        if (order.getState() != Order.State.nullVal) {//订单状态
            temp.setState(order.getState());
        }
        if (order.getAmount() >= 0) {//订单金额
            temp.setAmount(order.getAmount());
        }
        if (order.getRefund() >= 0 && order.getRefund() < temp.getDeposit()) {//退款金额小于押金
            temp.setRefund(order.getRefund());
        }
        if (order.getRentalDays() > 0) {//租赁天数
            temp.setRentalDays(order.getRentalDays());
        }
        orderService.update(temp);

        OrderDetail detail = new OrderDetail();
        detail.setTime(new Date().getTime());
        detail.setDescription("后台修改,用户订单");
        detail.setName("系统管理员");
        detail.setOrderNumber(order.getOrderNumber());
        orderService.add(detail);

        JsonResponse response = JsonResponse.success("成功");
        response.setData("order", temp);
        return response;
    }

    /**
     * 在提交订单前，商户应用需要先从时间戳查询接口请求到一个时间戳
     *
     * @return {"timestamp":"20150801111114","responseStatus":{"statusCode":200,"reasonPhrase":"请求正常完成","family":"SUCCESSFUL"}}
     */
    @ResponseBody
    @RequestMapping(value = "/getTimestamp.do")
    public String getTimestamp() {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = Constant.timestampurl + "?merchantNo=" + Constant.merchantNo;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += new String(line.getBytes(), "utf-8");
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 更新取机时间和还机时间
     */
    @ResponseBody
    @RequestMapping(value = "/updateTime.do")
    public JsonResponse updateTime(@RequestBody(required = false) Order order) {
        if (order == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (ValidateUtil.isEmpty(order.getOrderNumber())) {
            return JsonResponse.error("主键不能为空");
        }

        if (order.getNewObtainTime() == 0 && order.getNewReturnTime() == 0) {
            return JsonResponse.error("取机时间或还机时间不能为空");
        }

        Order temp = orderService.getById(Order.class, order.getOrderNumber());
        if (temp == null) {
            return JsonResponse.error("更新的订单不存在");
        }

        /** 计算当前订单修改后的日期是否包含机主设置的不可租赁时间*/
        // 修改后取机时间
        long realObtainTime = order.getNewObtainTime() > 0 ? order.getNewObtainTime() : temp.getObtainTime();
        // 修改后还机时间
        long realReturnTime = order.getNewReturnTime() > 0 ? order.getNewReturnTime() : temp.getReturnTime();

        //1. 如果机主设置了该时间段不允许出租
        if (rejectTimeService.hasRejectTimeInRange(order.getRentalUserId(), order.getRentalId(), realObtainTime, realReturnTime) > 0) {
            return JsonResponse.error("您选择的时间段,机主不希望出租相机");
        }

        return orderService.updateTime(order.getOrderNumber(), order.getNewObtainTime(), order.getNewReturnTime());
    }

    /**
     * 正常退款,预先计算退款信息,获取退款信息
     *
     * @param order
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getRefundInfo.do")
    public JsonResponse getRefundInfo(@RequestBody(required = false) Order order) {
        if (order == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (ValidateUtil.isEmpty(order.getOrderNumber())) {
            return JsonResponse.error("主键不能为空");
        }
        // 查询订单
        order = orderService.getById(Order.class, order.getOrderNumber());
        if (order == null) {
            return JsonResponse.error("订单不存在");
        }
        //订单金额增加量,修改时间导致订单时间增加,所以需要多扣除部分钱
        double addedAmount = 0;
        if (order.getNewAmount() > 0) {//如果更新过时间
            addedAmount = order.getNewAmount() - order.getAmount();
        }

        // 订单总金额=押金+租金+强制保险+免赔险
        // 退款金额 =订单总金额- 租金 - 强制保险 - 免赔险 - 订单金额增量
        double refound = order.getAmount() - order.getRentalAmount() - order.getInsurance() - order.getDeductibleInsurance();
        if (addedAmount > 0) {// 如果订单金额增加了,把本来需要多支付的部分从押金扣除
            refound = refound - addedAmount;
        }

        order.setRefund(refound);

        //将钱打入用户余额
        order.setRefundRental(Math.max(order.getRentalAmount(), order.getNewRentalAmount()) * 0.9);
        JsonResponse response = JsonResponse.success("操作成功");
        response.setData("order", order);
        return response;
    }

    /**
     * 从盛付通获取订单支付装太
     *
     * @param order
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getPayInfo.do")
    public JsonResponse getPayInfo(@RequestBody(required = false) Order order) throws Exception {
        if (order == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (ValidateUtil.isEmpty(order.getOrderNumber())) {
            return JsonResponse.error("主键不能为空");
        }
        // 查询订单
        order = orderService.getById(Order.class, order.getOrderNumber());
        if (order == null) {
            return JsonResponse.error("订单不存在");
        }

        OrderQueryRequest request = WSHelper.getInitOrderQueryRequest();
        request.setOrderNo(order.getOrderNumber());

        WSHelper.signOrderQueryRequest(request, Constant.merchantKey);
        WSProxy proxy = new WSProxy();
        OrderQueryResponse response = proxy.doQueryOrder(request);
        JsonResponse response1 = JsonResponse.success("查询成功");
        response1.setData("order", response);
        return response1;
    }
}
