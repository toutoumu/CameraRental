package com.dsfy.entity.pay;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.List;

/**
 * 代金券
 */
@Entity
@Table(name = "t_CashCoupon")
public class CashCoupon {
    public class State {
        public static final int nullVal = 0;
        /** 正常 */
        public static final int enable = 1;
        /** 作废 */
        public static final int disable = 2;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cashId;
    /** 代金券金额 */
    @Expose
    @Column
    private double amount;
    /** 邀请码 */
    @Expose
    @Column(length = 50)
    private String invitationCode;
    /** 代金券状态 */
    @Expose
    @Column
    private int state;
    /** 拥有者用户名 */
    @Expose
    @Column
    private String userName;
    /** 代金券是谁的 */
    @Expose
    @Column
    private int userId;
    /** 代金券是谁的(用户姓名) */
    @Expose
    @Column
    private String name;
    /** 代金券是谁的手机号码 */
    @Column
    @Expose
    private String phone;
    /** 订单编号(在哪个订单使用) */
    @Expose
    @Column
    private String orderNumber;
    /** 代金券获取时间 */
    @Expose
    @Column
    private long createTime;
    /** 使用代金券的时间 */
    @Expose
    @Column
    private long useTime;
    /** 过期日期 */
    @Expose
    @Column
    private long expireTime;
    /** 代金券种类 */
    @Expose
    @Column
    private int category;
    /** 描述代金券的作用 */
    @Expose
    @Column
    private String description;
    /** 备注 */
    @Expose
    @Column
    private String mark;

    /** 不会序列号到数据库 */
    @Transient
    private List<String> userIds;

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getPhone() {
        return phone;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getCashId() {
        return cashId;
    }

    public void setCashId(int cashId) {
        this.cashId = cashId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public void setName(String userName) {
        this.name = userName;
    }

    public long getUseTime() {
        return useTime;
    }

    public void setUseTime(long useTime) {
        this.useTime = useTime;
    }
}
