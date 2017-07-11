package com.dsfy.entity.pay;

import com.dsfy.entity.ImageInfo;
import com.dsfy.entity.RentalInfo;
import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.List;

/**
 * 订单
 */
@Entity
@Table(name = "t_order")
public class Order {
    /** 删除状态 */
    public static class DeleteState {
        /** 空 */
        public static final int nullVal = 0;
        /** 未删除 */
        public static final int unDelete = 1;
        /** 已删除 */
        public static final int deleted = 2;
    }

    /** 订单状态 */
    public static class State {
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
        /** 争议已经解决,等待退款回调 */
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

    }

    /** 取机|还机 时间是否被更新过 */
    public static class TimeUpdated {
        /** 空 */
        public static final int nullVal = 0;
        /** 未更新 */
        public static final int unUpdated = 1;
        /** 已更新 */
        public static final int updated = 2;
    }


    public static class Shared {
        /** 空 */
        public static final int nullVal = 0;
        /** 未分分享 */
        public static final int unShared = 1;
        /** 已经分享 */
        public static final int shared = 2;
    }

    @Id
    @Expose
    @Column(name = "orderId", nullable = false)
    private String orderNumber;
    /** 租客ID */
    @Expose
    @Column(nullable = false)
    private int userId;
    /** 租客姓名 */
    @Expose
    @Column
    private String name;
    /** 租客昵称 */
    @Expose
    @Column
    private String nickName;
    /** 机主ID */
    @Expose
    @Column
    private int rentalUserId;
    /** 机主姓名 */
    @Expose
    @Column
    private String rentalName;
    /** 机主昵称 */
    @Expose
    @Column
    private String rentalNickName;
    /** 租赁信息ID */
    @Expose
    @Column(nullable = false)
    private int rentalId;
    /** 租赁天数 */
    @Expose
    @Column
    private int rentalDays;
    /** 第一天的价格,随后按照25%计算 单位 分(这个价格来自租赁信息) */
    @Expose
    @Column
    private double price;
    /** 押金 单位分(这个值来自租赁信息) */
    @Expose
    @Column
    private double deposit;
    /** 支付现金金额 */
    @Expose
    @Column
    private double payAmount;

    /** 租金 不含保险 订单完成后90%归机主 */
    @Expose
    @Column
    private double rentalAmount;
    /** 强制保险金 租金*5% */
    @Expose
    @Column
    private double insurance;
    /** 免赔险 租金*15% */
    @Expose
    @Column
    private double deductibleInsurance;
    /** 订单总金额 单位分 订单总金额=押金+租金+强制保险+免赔险 */
    @Expose
    @Column
    private double amount;
    /** 租客退款金额 单位分 退还给租客 */
    @Expose
    @Column
    private double refund;
    /** 退还的租金(给机主) rentalAmount * 0.9 */
    @Expose
    @Column
    private double refundRental;

    /** 代金券ID */
    @Expose
    @Column
    private int cashId;
    /** 代金券金额 */
    @Expose
    @Column
    private double cashAmount;

    /** 取机时间 */
    @Expose
    @Column
    private long obtainTime;
    /** 还机时间 */
    @Expose
    @Column
    private long returnTime;
    /** 订单创建时间 */
    @Expose
    @Column
    private long createTime;
    /** 订单结束时间 */
    @Expose
    @Column
    private long finishTime;
    @Expose
    @Column
    private long accetpTime;
    /** 订单状态 */
    @Expose
    @Column
    private int state;
    /** 是否删除 0.未知,1.未删除,2.已删除 */
    @Column
    @Expose
    private String brand;
    @Column
    @Expose
    private String model;
    @Expose
    @Column
    private String cover;
    @Expose
    @Column(name = "isDelete")
    private int deleted;
    /** 备注说明 */
    @Expose
    @Column(length = 400)
    private String mark;
    /** 分享 */
    @Expose
    @Column
    private int share;
    /** 理赔理由(原因) */
    @Expose
    @Column
    private String reason;

    /** 新租赁天数 */
    @Expose
    @Column
    private int newRentalDays;
    /** 租金 不含保险 订单完成后90%归机主 */
    @Expose
    @Column
    private double newRentalAmount;
    /** 强制保险金 租金*5% */
    @Expose
    @Column
    private double newInsurance;
    /** 免赔险 租金*15% */
    @Expose
    @Column
    private double newDeductibleInsurance;
    /** 订单总金额 单位分 订单总金额=押金+租金+强制保险+免赔险 */
    @Expose
    @Column
    private double newAmount;
    /** 租客退款金额 单位分 退还给租客 */
    //@Expose
    //@Column
    //private double newRefund;
    /** 退还的租金(给机主) rentalAmount * 0.9 */
    //@Expose
    //@Column
    //private double newRefundRental;
    /** 新的取机时间 */
    @Expose
    @Column
    private long newObtainTime;
    /** 新的还机时间 */
    @Expose
    @Column
    private long newReturnTime;
    /** 收货地址 */
    @Expose
    @Column(length = 255)
    private String address;

    /******************************** 下面数据不会保存到数据库 *************************/
    /** 租赁信息 */
    @Transient
    @Expose
    private RentalInfo rentalInfo;
    /** 相机交接时候拍摄的照片 */
    @Transient
    @Expose
    private List<ImageInfo> demos;
    /** 有争议时候拍摄的照片 */
    @Transient
    @Expose
    private List<ImageInfo> disputeImage;
    /** 代金券 */
    @Transient
    @Expose
    private List<CashCoupon> cashCoupons;
    /** 订单处理详情 */
    @Transient
    @Expose
    private List<OrderDetail> details;

    /** 争议订单,退还租客金额(只在处理争议时作为请求参数使用) */
    @Transient
    @Expose
    private double disputeRefund;
    /** 争议订单,退还机主金额(只在处理争议时作为请求参数使用) */
    @Transient
    @Expose
    private double disputeRefundRental;


    public int getNewRentalDays() {
        return newRentalDays;
    }

    public void setNewRentalDays(int newRentalDays) {
        this.newRentalDays = newRentalDays;
    }

    public double getNewRentalAmount() {
        return newRentalAmount;
    }

    public void setNewRentalAmount(double newRentalAmount) {
        this.newRentalAmount = newRentalAmount;
    }

    public double getNewInsurance() {
        return newInsurance;
    }

    public void setNewInsurance(double newInsurance) {
        this.newInsurance = newInsurance;
    }

    public double getNewDeductibleInsurance() {
        return newDeductibleInsurance;
    }

    public void setNewDeductibleInsurance(double newDeductibleInsurance) {
        this.newDeductibleInsurance = newDeductibleInsurance;
    }

    public double getNewAmount() {
        return newAmount;
    }

    public void setNewAmount(double newAmount) {
        this.newAmount = newAmount;
    }

   /* public double getNewRefund() {
        return newRefund;
    }

    public void setNewRefund(double newRefund) {
        this.newRefund = newRefund;
    }

    public double getNewRefundRental() {
        return newRefundRental;
    }

    public void setNewRefundRental(double newRefundRental) {
        this.newRefundRental = newRefundRental;
    }*/

    /**
     * 获取真实的取机时间
     *
     * @return
     */
    public long getRealObtainTime() {
        if (newObtainTime > 0) {
            return newObtainTime;
        }
        return obtainTime;
    }

    /**
     * 获取真实的还机时间
     *
     * @return
     */
    public long getRealReturnTime() {
        if (newReturnTime > 0) {
            return newReturnTime;
        }
        return returnTime;
    }

    public long getNewObtainTime() {
        return newObtainTime;
    }

    public void setNewObtainTime(long newObtainTime) {
        this.newObtainTime = newObtainTime;
    }

    public long getNewReturnTime() {
        return newReturnTime;
    }

    public void setNewReturnTime(long newReturnTime) {
        this.newReturnTime = newReturnTime;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getRentalUserId() {
        return rentalUserId;
    }

    public void setRentalUserId(int rentalUserId) {
        this.rentalUserId = rentalUserId;
    }

    public String getRentalName() {
        return rentalName;
    }

    public void setRentalName(String rentalName) {
        this.rentalName = rentalName;
    }

    public String getRentalNickName() {
        return rentalNickName;
    }

    public void setRentalNickName(String rentalNickName) {
        this.rentalNickName = rentalNickName;
    }

    public int getRentalId() {
        return rentalId;
    }

    public void setRentalId(int rentalId) {
        this.rentalId = rentalId;
    }

    public int getRentalDays() {
        return rentalDays;
    }

    public void setRentalDays(int rentalDays) {
        this.rentalDays = rentalDays;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }

    public double getRentalAmount() {
        return rentalAmount;
    }

    public void setRentalAmount(double rentalAmount) {
        this.rentalAmount = rentalAmount;
    }

    public double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(double payAmount) {
        this.payAmount = payAmount;
    }

    public double getInsurance() {
        return insurance;
    }

    public void setInsurance(double insurance) {
        this.insurance = insurance;
    }

    public double getDeductibleInsurance() {
        return deductibleInsurance;
    }

    public void setDeductibleInsurance(double deductibleInsurance) {
        this.deductibleInsurance = deductibleInsurance;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getRefund() {
        return refund;
    }

    public void setRefund(double refund) {
        this.refund = refund;
    }

    public double getRefundRental() {
        return refundRental;
    }

    public void setRefundRental(double refundRental) {
        this.refundRental = refundRental;
    }

    public int getCashId() {
        return cashId;
    }

    public void setCashId(int cashId) {
        this.cashId = cashId;
    }

    public double getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(double cashAmount) {
        this.cashAmount = cashAmount;
    }

    public long getObtainTime() {
        return obtainTime;
    }

    public void setObtainTime(long obtainTime) {
        this.obtainTime = obtainTime;
    }

    public long getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(long returnTime) {
        this.returnTime = returnTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }

    public long getAccetpTime() {
        return accetpTime;
    }

    public void setAccetpTime(long accetpTime) {
        this.accetpTime = accetpTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public int getShare() {
        return share;
    }

    public void setShare(int share) {
        this.share = share;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public RentalInfo getRentalInfo() {
        return rentalInfo;
    }

    public void setRentalInfo(RentalInfo rentalInfo) {
        this.rentalInfo = rentalInfo;
    }

    public List<ImageInfo> getDemos() {
        return demos;
    }

    public void setDemos(List<ImageInfo> demos) {
        this.demos = demos;
    }

    public List<ImageInfo> getDisputeImage() {
        return disputeImage;
    }

    public void setDisputeImage(List<ImageInfo> disputeImage) {
        this.disputeImage = disputeImage;
    }

    public List<CashCoupon> getCashCoupons() {
        return cashCoupons;
    }

    public void setCashCoupons(List<CashCoupon> cashCoupons) {
        this.cashCoupons = cashCoupons;
    }

    public List<OrderDetail> getDetails() {
        return details;
    }

    public void setDetails(List<OrderDetail> details) {
        this.details = details;
    }

    /**
     * 争议订单退还租客金额
     *
     * @return
     */
    public double getDisputeRefund() {
        return disputeRefund;
    }

    /**
     * 争议订单退还租客金额
     *
     * @param disputeRefund
     */
    public void setDisputeRefund(double disputeRefund) {
        this.disputeRefund = disputeRefund;
    }

    /**
     * 争议订单退还机主金额
     *
     * @return
     */
    public double getDisputeRefundRental() {
        return disputeRefundRental;
    }

    /**
     * 争议订单退还机主金额
     *
     * @param disputeRefundRental
     */
    public void setDisputeRefundRental(double disputeRefundRental) {
        this.disputeRefundRental = disputeRefundRental;
    }
}
