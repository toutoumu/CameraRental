package com.dsfy.entity.pay;

import com.google.gson.annotations.Expose;

import javax.persistence.*;

/**
 * 订单流程明细
 */
@Entity
@Table(name = "t_OrderDetail")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    @Column
    private int detailId;
    /** 订单编号 */
    @Expose
    @Column
    private String orderNumber;
    /** 处理时间 */
    @Column
    private long time;
    /** 处理人 */
    @Column
    private int userId;
    /** 处理人姓名 */
    @Column
    private String name;
    /** 描述 */
    @Column
    private String description;

    public int getDetailId() {
        return detailId;
    }

    public void setDetailId(int detailId) {
        this.detailId = detailId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
