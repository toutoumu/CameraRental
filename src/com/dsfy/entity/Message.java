package com.dsfy.entity;

import com.google.gson.annotations.Expose;

import javax.persistence.*;

@Entity
@Table(name = "t_Message")
public class Message {

    /** 订单状态 */
    public static class Category {
        /** 进入理赔流程(订单有争议) */
        public static final int hasDispute = -3;
        /** 空(这种状态不会出现) */
        public static final int nullVal = 0;
        /** 租客已经下单 */
        public static final int ordered = 1;
        /** 机主接受订单(等待租客支付) */
        public static final int accepted = 2;
        /** 租客已经支付押金(等待第三方回调) */
        public static final int waitingPaymentCallBack = 3;
        /** 租客支付完成(paid) */
        public static final int paymented = 4;
        /** 机主已经交机 */
        public static final int obtainedNotice = 5;
        /** 租客确认取机 */
        public static final int obtained = 6;
        /** 租客已经还机 */
        public static final int returnedNotice = 7;
        /** 机主确认还机(同时申请退还押金给租客) */
        public static final int returned = 8;
        /** 后台发起退款(等待退款回调) */
        public static final int waitingRefundCallBack = 9;
        /** 退款完成 */
        public static final int refunded = 10;
        /** 争议已经解决 */
        public static final int disputeFinish = 11;
        /** 用户已经评论(订单关闭不允许再修改) */
        public static final int isclosed = 12;

        /*************************************以下是订单异常结束******************************************/
        /** 租客支付超时 */
        public static final int paymentTimeout = 13;
        /** 租客取消订单 */
        public static final int userCanceled = 14;
        /** 机主接受订单超时 */
        public static final int acceptTimeout = 15;
        /** 机主拒绝接单 */
        public static final int reject = 16;


        /** 机主接受订单超时---检查 */
        public static final int checkOrderAccept = 17;
        /** 租客支付超时------检查 */
        public static final int checkOrderPay = 18;

        /** 取机消息推送------推送 */
        public static final int pushObtain = 19;
        /** 还机消息推送------推送 */
        public static final int pushReturn = 20;
        /** 代金券推送 */
        public static final int cashCoupon = 21;
    }

    /** 消息是否处理完成 */
    public static class IsFinish {
        /** 空值 */
        public static final int nullVal = 0;
        /** 已经完成 */
        public static final int finish = 1;
        /** 没有完成 */
        public static final int unFinish = 2;

        public static final int _errror = 3;
    }


    /** 消息Id */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    @Column(name = "messageId")
    private int messageId;
    /** 发送时间 */
    @Expose
    @Column
    private long sendTime;
    /** 消息类型 */
    @Expose
    @Column
    private int category;
    /** 订单编号 */
    @Expose
    @Column
    private String orderNumber;

    /** 租客电话号码,推送时候的tag短信发送的号码 */
    @Expose
    @Column
    private String guestPhone;
    /** 机主电话 */
    @Column
    @Expose
    private String adminPhone;

    /** 消息是否发送完成 */
    @Expose
    @Column
    private int isFinish = IsFinish.unFinish;

    /** 短信消息模版Id */
    @Expose
    @Column
    private String templateId;

    /** 短信消息参数,短信(短信参数以,分割),推送消息(消息内容) */
    @Expose
    @Column
    private String parameters;

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getGuestPhone() {
        return guestPhone;
    }

    public void setGuestPhone(String guestPhone) {
        this.guestPhone = guestPhone;
    }

    public String getAdminPhone() {
        return adminPhone;
    }

    public void setAdminPhone(String adminPhone) {
        this.adminPhone = adminPhone;
    }

    public int getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(int isFinish) {
        this.isFinish = isFinish;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;
        return messageId == message.messageId;
    }

    @Override
    public int hashCode() {
        return messageId;
    }
}
