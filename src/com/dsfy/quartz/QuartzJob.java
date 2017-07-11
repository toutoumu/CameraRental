package com.dsfy.quartz;

import cn.jpush.api.JPushClient;
import com.dsfy.entity.Message;
import com.dsfy.entity.authority.SysmanUser;
import com.dsfy.entity.pay.Order;
import com.dsfy.service.IMessageService;
import com.dsfy.service.IUserService;
import com.dsfy.util.Constant;
import com.dsfy.util.JsonUtils;
import com.dsfy.util.ValidateUtil;

import java.util.List;

/**
 * 执行Quartz任务
 */
public class QuartzJob {

    JPushClient pushClient;
    IMessageService messageService;

    IUserService userService;

    public IMessageService getMessageService() {
        return messageService;
    }

    public void setMessageService(IMessageService messageService) {
        this.messageService = messageService;
    }

    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    public QuartzJob() {
        pushClient = new JPushClient(Constant.masterSecret, Constant.appKey, 2);
    }

    /**
     * Spring配置的执行的任务方法
     */
    public void doJob() {
        // 1. 获取未处理的消息
        List<Message> unHandlerMessage = messageService.getUnHandlerMessage();
        if (unHandlerMessage != null && unHandlerMessage.size() > 0) {
            // 2. 处理消息
            for (Message message : unHandlerMessage) {
                // 代金券消息
                if (message.getCategory() == Message.Category.cashCoupon) {
                    message.setIsFinish(Message.IsFinish.finish);//已经完成
                    this.sendJpush(message);
                    messageService.update(message);
                    continue;
                }
                //貌似都需要用到order所以在这里直接先查询出来
                Order order = messageService.getById(Order.class, message.getOrderNumber());
                message.setIsFinish(Message.IsFinish.finish);//已经完成
                switch (message.getCategory()) {
                    case Message.Category.hasDispute: {//订单有争议
                        this.sendJpush(message);
                        break;
                    }
                    case Message.Category.ordered: {//租客已经下单
                        this.sendJpush(message);
                        break;
                    }
                    case Message.Category.accepted: {//机主接受订单,通知租客支付押金
                        this.sendJpush(message);
                        break;
                    }
                    case Message.Category.waitingPaymentCallBack: {//已经支付到第三方账户,等待回调
                        this.sendJpush(message);
                        break;
                    }
                    case Message.Category.paymented: {//支付成功
                        this.sendJpush(message);
                        break;
                    }
                    case Message.Category.obtainedNotice: {//机主已经交出相机
                        this.sendJpush(message);
                        break;
                    }
                    case Message.Category.obtained: {//租客已经获得相机
                        this.sendJpush(message);
                        break;
                    }
                    case Message.Category.returnedNotice: {//租客已经归还相机
                        this.sendJpush(message);
                        break;
                    }
                    case Message.Category.returned: {//机主确认还机
                        this.sendJpush(message);
                        break;
                    }
                    case Message.Category.waitingRefundCallBack: {//已经向第三方发起退款申请
                        this.sendJpush(message);
                        break;
                    }
                    case Message.Category.refunded: {//退款成功
                        this.sendJpush(message);
                        break;
                    }
                    case Message.Category.disputeFinish: {//争议订单处理完成
                        this.sendJpush(message);
                        break;
                    }
                    case Message.Category.isclosed: {//订单已经评论,关闭
                        break;
                    }
                    case Message.Category.paymentTimeout: {//租客支付超时
                        this.sendJpush(message);
                        break;
                    }
                    case Message.Category.userCanceled: {//租客取消订单
                        this.sendJpush(message);
                        break;
                    }
                    case Message.Category.acceptTimeout: {//机主接受超时
                        this.sendJpush(message);
                        break;
                    }
                    case Message.Category.reject: {//发送拒绝接单的短信给租客,重新选择
                        this.sendJpush(message);
                        //MessageSend.sendSMSByTemplate(message.getPhone(), "模板ID", "参数,参数");
                        break;
                    }

                    /**下面是检测订单状态*/
                    case Message.Category.checkOrderAccept: {//订单检查,检查订单接受是否已经 [超时]
                        this.sendJpush(message);
                        acceptTimeOut(message, order);
                        break;
                    }
                    case Message.Category.checkOrderPay: {//订单支付 [超时] 检测
                        this.sendJpush(message);
                        payTimeOut(message, order);
                        break;
                    }
                    case Message.Category.pushObtain: {//取机消息推送,短信等等,需要发送给机主和租客
                        this.sendJpush(message);
                        //MessageSend.sendSMSByTemplate(message.getPhone(), "模板ID", "参数,参数");
                        break;
                    }
                    case Message.Category.pushReturn: {//还机消息推送,短信等等,需要发送给机主和租客
                        this.sendJpush(message);
                        //MessageSend.sendSMSByTemplate(message.getPhone(), "模板ID", "参数,参数");
                        break;
                    }
                }
                // 处理完成后删除消息
                messageService.update(message);
                System.out.println("成功处理一条消息消息类型:" + message.getCategory());
            }
        }
    }

    /**
     * 租客支付超时
     *
     * @param message
     * @param order
     */
    private void payTimeOut(Message message, Order order) {
        if (!ValidateUtil.isEmpty(message.getOrderNumber())) {
            if (order != null) {
                if (order.getState() == Order.State.accepted) {
                    //租客支付订单超时,更新订单状态为机主接受超时
                    order.setState(Order.State.paymentTimeout);
                    messageService.update(order);
                    //发送短信给机主 支付超时
                    // MessageSend.sendSMSByTemplate(message.getPhone(), "模板ID", "参数,参数");
                }
            }
        }
    }

    /**
     * 机主接单超时
     *
     * @param message
     * @param order
     */
    private void acceptTimeOut(Message message, Order order) {
        if (!ValidateUtil.isEmpty(message.getOrderNumber())) {
            if (order != null) {
                if (order.getState() == Order.State.ordered) {
                    //机主接受订单超时,更新订单状态为机主接受超时
                    order.setState(Order.State.acceptTimeout);
                    messageService.update(order);
                    //发送短信给租客 重新选择
                    // MessageSend.sendSMSByTemplate(message.getPhone(), "模板ID", "参数,参数");
                }
            }
        }
    }

    /**
     * 消息推送
     *
     * @param message 消息内容
     */
    private void sendJpush(Message message) {
        SysmanUser guest = userService.getByUserName(message.getGuestPhone());
        if (guest != null) {
            try {
                if (!ValidateUtil.isEmpty(message.getGuestPhone()) && "android".equals(guest.getDevice())) {
                    System.out.println("推送内容" + JsonUtils.toJson(message));
                    pushClient.sendAndroidMessageWithAlias("标题", JsonUtils.toJson(message), message.getGuestPhone());
                }
            } catch (Exception e) {
                message.setIsFinish(Message.IsFinish._errror);//处理失败
                e.printStackTrace();
            }
            try {
                if (!ValidateUtil.isEmpty(message.getGuestPhone()) && "ios".equals(guest.getDevice())) {
                    System.out.println("推送内容" + JsonUtils.toJson(message));
                    pushClient.sendIosMessageWithAlias("标题", JsonUtils.toJson(message), message.getGuestPhone());
                }
            } catch (Exception e) {
                message.setIsFinish(Message.IsFinish._errror);//处理失败
                e.printStackTrace();
            }
        }
        SysmanUser admin = userService.getByUserName(message.getAdminPhone());
        if (admin != null) {
            try {
                if (!ValidateUtil.isEmpty(message.getAdminPhone()) && "android".equals(admin.getDevice())) {
                    System.out.println("推送内容" + JsonUtils.toJson(message));
                    pushClient.sendAndroidMessageWithAlias("标题", JsonUtils.toJson(message), message.getAdminPhone());
                }
            } catch (Exception e) {
                message.setIsFinish(Message.IsFinish._errror);//处理失败
                e.printStackTrace();
            }

            try {
                if (!ValidateUtil.isEmpty(message.getAdminPhone()) && "ios".equals(admin.getDevice())) {
                    System.out.println("推送内容" + JsonUtils.toJson(message));
                    pushClient.sendIosMessageWithAlias("标题", JsonUtils.toJson(message), message.getAdminPhone());
                }
            } catch (Exception e) {
                message.setIsFinish(Message.IsFinish._errror);//处理失败
                e.printStackTrace();
            }
        }
    }
}
