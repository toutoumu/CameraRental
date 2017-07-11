package com.dsfy.controller;

import com.dsfy.dao.util.Pagination;
import com.dsfy.dao.util.QueryCondition;
import com.dsfy.entity.Message;
import com.dsfy.entity.authority.SysmanUser;
import com.dsfy.entity.http.JsonResponse;
import com.dsfy.entity.pay.CashCoupon;
import com.dsfy.entity.pay.Order;
import com.dsfy.service.ICashCouponService;
import com.dsfy.service.IUserService;
import com.dsfy.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 代金券
 */
@Controller
@RequestMapping(value = "/CashCouponController", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
public class CashCouponController {
    @Resource(name = "CashCouponService")
    private ICashCouponService cashCouponService;
    @Resource(name = "UserService")
    private IUserService userService;

    /**
     * 添加代金券
     *
     * @param cashCoupon
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/add.do")
    public JsonResponse add(@RequestBody(required = false) CashCoupon cashCoupon) {
        if (cashCoupon == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (cashCoupon.getUserId() == 0) {
            return JsonResponse.error("拥有者不能为空");
        }
        if (cashCoupon.getExpireTime() == 0) {
            return JsonResponse.error("代金券过期日期不能为空");
        }
        // 更新订单为已经被分享
        if (!ValidateUtil.isEmpty(cashCoupon.getOrderNumber())) {
            Order order = cashCouponService.getById(Order.class, cashCoupon.getOrderNumber());
            order.setShare(Order.Shared.shared);//已经分享
            cashCouponService.update(order);
        }

        SysmanUser user = cashCouponService.getById(SysmanUser.class, cashCoupon.getUserId());
        if (user == null) {
            return JsonResponse.error("用户不存在");
        }
        cashCoupon.setName(user.getRealName());//拥有者姓名
        cashCoupon.setState(CashCoupon.State.enable);//可用
        cashCoupon.setCreateTime(new Date().getTime());//获取时间
        cashCoupon.setMark("分享订单获得代金券");
        cashCouponService.add(cashCoupon);
        JsonResponse jsonResponse = JsonResponse.success("成功");
        jsonResponse.setData("cashCoupon", cashCoupon);
        return jsonResponse;
    }

    /**
     * 删除代金券
     *
     * @param cashCoupon
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delete.do")
    public JsonResponse delete(@RequestBody(required = false) CashCoupon cashCoupon) {
        if (cashCoupon == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (cashCoupon.getCashId() == 0) {
            return JsonResponse.error("主键不能为空");
        }
        CashCoupon temp = cashCouponService.getById(CashCoupon.class, cashCoupon.getCashId());
        if (temp == null) {
            return JsonResponse.error("删除的代金券不存在");
        }
        cashCouponService.delete(CashCoupon.class, cashCoupon.getCashId());
        return JsonResponse.success("删除成功");
    }

    /**
     * 更新代金券
     *
     * @param cashCoupon
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/update.do")
    public JsonResponse update(@RequestBody(required = false) CashCoupon cashCoupon) {
        if (cashCoupon == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (cashCoupon.getCashId() == 0) {
            return JsonResponse.error("主键不能为空");
        }
        if (ValidateUtil.isEmpty(cashCoupon.getName())) {
            return JsonResponse.error("拥有者名称不能为空");
        }
        CashCoupon temp = cashCouponService.getById(CashCoupon.class, cashCoupon.getCashId());
        if (temp == null) {
            return JsonResponse.error("更新的代金券不存在");
        }
        if (!ValidateUtil.isEmpty(cashCoupon.getDescription())) {//描述
            temp.setDescription(cashCoupon.getDescription());
        }
        if (!ValidateUtil.isEmpty(cashCoupon.getMark())) {//备注
            temp.setMark(cashCoupon.getMark());
        }
        if (cashCoupon.getCreateTime() > 0) {//获取时间
            temp.setCreateTime(cashCoupon.getCreateTime());
        }
        if (cashCoupon.getUseTime() > 0) {//使用时间
            temp.setUseTime(cashCoupon.getUseTime());
        }
        if (cashCoupon.getExpireTime() > 0) {//过期时间
            temp.setExpireTime(cashCoupon.getExpireTime());
        }
        if (cashCoupon.getAmount() > 0) {
            temp.setAmount(cashCoupon.getAmount());
        }
        if (cashCoupon.getState() != CashCoupon.State.nullVal) {
            temp.setState(cashCoupon.getState());
        }

        cashCouponService.update(temp);
        return JsonResponse.success("更新成功");
    }

    /**
     * 获取所有代金券
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getByUser.do")
    public JsonResponse getByUser(@RequestBody(required = false) SysmanUser user) {
        if (user == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (user.getPid() == null || user.getPid() == 0) {
            return JsonResponse.error("用户ID不能为空");
        }
        // 根据用户ID查询
        List<CashCoupon> cashCoupons = cashCouponService.get(CashCoupon.class, new QueryCondition("userId", QueryCondition.EQ, user.getPid()));
        JsonResponse response = JsonResponse.success("查询成功");
        response.setData("cashCoupons", cashCoupons);
        return response;
    }


    @ResponseBody
    @RequestMapping(value = "/getById.do")
    public JsonResponse getById(@RequestBody(required = false) CashCoupon cashCoupon) {
        if (cashCoupon == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (cashCoupon.getCashId() == 0) {
            return JsonResponse.error("主键不能为空");
        }
        cashCoupon = cashCouponService.getById(CashCoupon.class, cashCoupon.getCashId());

        JsonResponse response = JsonResponse.success("查询成功");
        response.setData("cashCoupon", cashCoupon);
        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/query.do")
    public JsonResponse query(@RequestBody(required = false) Pagination<CashCoupon> pagination) {
        if (pagination == null || pagination.getData() == null) {
            return JsonResponse.error("查询条件不能为空");
        }

        Pagination<CashCoupon> result = cashCouponService.query(pagination.getData(), pagination.getCurrentPage(), pagination.getPageSize());
        JsonResponse response = JsonResponse.success("成功");
        response.setData("pagination", result);
        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/disable.do")
    public JsonResponse disable(@RequestBody(required = false) CashCoupon cashCoupon) {
        if (cashCoupon == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (cashCoupon.getCashId() == 0) {
            return JsonResponse.error("主键不能为空");
        }
        cashCoupon = cashCouponService.getById(CashCoupon.class, cashCoupon.getCashId());
        if (cashCoupon == null) {
            return JsonResponse.error("作废的代金券不存在");
        }
        if (cashCoupon.getUseTime() > 0) {
            return JsonResponse.error("代金券已经被使用");
        }
        cashCoupon.setState(CashCoupon.State.disable);
        cashCouponService.update(cashCoupon);
        return JsonResponse.success("作废成功");
    }

    /**
     * 批量发放代金券, 用Mark传递推送内容
     *
     * @param cashCoupon
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/batch.do")
    public JsonResponse batch(@RequestBody(required = false) CashCoupon cashCoupon) {
        // 1. 输入参数校验
        if (cashCoupon == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (cashCoupon.getUserIds() == null || cashCoupon.getUserIds().size() == 0) {
            return JsonResponse.error("拥有者不能为空");
        }
        if (cashCoupon.getAmount() <= 0) {
            return JsonResponse.error("代金券金额不能为零");
        }
        if (cashCoupon.getExpireTime() == 0) {
            return JsonResponse.error("代金券过期日期不能为空");
        }

        // 2. 保存代金券, 和消息推送内容
        double amount = cashCoupon.getAmount(); //代金券金额
        long ex = cashCoupon.getExpireTime(); //代金券过期时间
        String mark = cashCoupon.getMark();//消息推送内容

        List<CashCoupon> cashCoupons = new ArrayList<>();
        List<Message> messages = new ArrayList<>();
        for (String s : cashCoupon.getUserIds()) {
            SysmanUser user = cashCouponService.getById(SysmanUser.class, Integer.parseInt(s));
            if (user == null) {
                return JsonResponse.error("用户不存在");
            }
            // 如果推送消息内容不为空
            if (!ValidateUtil.isEmpty(mark)) {
                Message message = new Message();
                message.setCategory(Message.Category.cashCoupon);
                message.setParameters(mark);
                message.setGuestPhone(user.getPhone());
                message.setIsFinish(Message.IsFinish.unFinish);
                message.setSendTime(new Date().getTime());
                messages.add(message);
            }

            // 代金券
            cashCoupon = new CashCoupon();
            cashCoupon.setAmount(amount);
            cashCoupon.setExpireTime(ex);
            cashCoupon.setUserId(user.getPid());//用户id
            cashCoupon.setUserName(user.getUserName());//用户名
            cashCoupon.setName(user.getRealName());//用户姓名
            cashCoupon.setPhone(user.getPhone());//用户手机
            cashCoupon.setState(CashCoupon.State.enable);//可用
            cashCoupon.setCreateTime(new Date().getTime());//获取时间
            cashCoupon.setMark("后台派发的代金券");
            cashCoupons.add(cashCoupon);
        }

        if (messages.size() > 0) {
            cashCouponService.batchSave(messages);
        }
        if (cashCoupons.size() > 0) {
            cashCouponService.batchSave(cashCoupons);
        }

        JsonResponse jsonResponse = JsonResponse.success("成功");
        jsonResponse.setData("cashCoupon", cashCoupon);
        return jsonResponse;
    }
}
