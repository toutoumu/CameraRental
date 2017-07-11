package com.dsfy.entity.pay;

import com.google.gson.annotations.Expose;

import javax.persistence.*;

/**
 * 用户账单(充值|消费记录)
 *
 * @author toutoumu
 */
@Entity
@Table(name = "t_TradeRecord")
public class TradeRecord {
    /** 删除状态 */
    public static class DeleteState {
        /** 空 */
        public static final int nullVal = 0;
        /** 未删除 */
        public static final int unDelete = 1;
        /** 已删除 */
        public static final int deleted = 2;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    @Column(name = "tradeId")
    private int tradeId;

    /** 用户ID */
    @Expose
    @Column
    private int userId;
    /** 用户昵称 */
    @Expose
    @Column
    private String nickname;
    /** 用户名 */
    @Expose
    @Column
    private String name;
    /** 订单ID */
    @Expose
    @Column
    private String orderNumber;
    /** 金额单位为分 */
    @Expose
    @Column
    private double amount;
    /** 消费类型（1充值,2消费) */
    @Expose
    @Column
    private int type;
    /** 创建时间 */
    @Expose
    @Column
    private long time;
    /** 对记录的描述 */
    @Expose
    @Column
    private String description;
    /** 是否删除 0.未知,1.未删除,2.已删除 */
    @Expose
    @Column(name = "isDelete")
    private int deleted;
    /** 备注说明 */
    @Expose
    @Column
    private String mark;

    public int setTradeId() {
        return tradeId;
    }

    public void setTradeId(int tradeId) {
        this.tradeId = tradeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /** 是否删除 0.未知,1.未删除,2.已删除 */
    public int getDeleted() {
        return deleted;
    }

    /** 是否删除 0.未知,1.未删除,2.已删除 */
    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
}