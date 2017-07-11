package com.dsfy.entity.pay;


import com.google.gson.annotations.Expose;

import javax.persistence.*;

@Entity
@Table(name = "t_Withdrawals")
public class Withdrawals {
    public static class State {
        public static final int nullVal = 0;
        /** 未退款 */
        public static final int unFinish = 1;
        /** 已经退款 */
        public static final int finished = 2;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    @Column(name = "WithdrawalsId")
    private int withdrawalsId;
    @Column
    @Expose
    private int userId;
    /**
     * 用户名
     */
    @Expose
    @Column(nullable = false, length = 50)
    private String userName;

    @Column(length = 20)
    @Expose
    private String phone;

    /**
     * 真实姓名
     */
    @Column(name = "real_name", length = 10)
    @Expose
    private String realName;
    @Column(length = 30)
    @Expose
    private String idCard;
    @Column(length = 20)
    @Expose
    private double amount;
    /** 处理状态 */
    @Column(length = 20)
    @Expose
    private int state;
    /** 申请时间 */
    @Column
    @Expose
    private long applyTime;
    /** 处理完成时间 */
    @Column
    @Expose
    private long finishTime;

    public int getWithdrawalsId() {
        return withdrawalsId;
    }

    public void setWithdrawalsId(int withdrawalsId) {
        this.withdrawalsId = withdrawalsId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public long getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(long applyTime) {
        this.applyTime = applyTime;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRealName() {
        return realName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
