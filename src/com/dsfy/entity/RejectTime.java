package com.dsfy.entity;

import com.dsfy.entity.authority.BaseEntity;
import com.google.gson.annotations.Expose;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_RejectTime")
public class RejectTime extends BaseEntity {

    /** 用户ID */
    @Column
    @Expose
    private int userId;
    /** 租赁信息ID */
    @Column
    @Expose
    private int rentalId;
    /** 拒绝接单时间,相对1970年的时间 */
    @Column(name = "mTime")
    @Expose
    private long time;
    /** 拒绝接单时间20150706 */
    @Column
    @Expose
    private int timeStr;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRentalId() {
        return rentalId;
    }

    public void setRentalId(int rentalId) {
        this.rentalId = rentalId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(int timeStr) {
        this.timeStr = timeStr;
    }

}
