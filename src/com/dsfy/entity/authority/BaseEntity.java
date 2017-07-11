package com.dsfy.entity.authority;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.gson.annotations.Expose;
import org.hibernate.annotations.GenericGenerator;


/**
 * @author Administrator
 */
@MappedSuperclass
public class BaseEntity implements Serializable {

    /** 删除状态 */
    public static class DeleteFlag {
        /** 空 */
        public static final int nullVal = 0;
        /** 未删除 */
        public static final int unDelete = 1;
        /** 已删除 */
        public static final int deleted = 2;
    }

    /**
     * 主键标示
     */
    @Id
    @GeneratedValue(generator = "_increment")
    @GenericGenerator(name = "_increment", strategy = "increment")
    @Column(length = 32)
    @Expose
    private Integer pid;

    /**
     * 删除标志  0 空 1未删除，2 已删除
     */
    @Column(precision = 1, name = "delete_flag")
    @Expose
    private int deleteFlag = DeleteFlag.unDelete;

    /**
     * 创建时间
     */
    @Expose
    @Column
    private long createTime = new Date().getTime();

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public int getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

}