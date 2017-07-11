package com.dsfy.entity.equipment;

import com.google.gson.annotations.Expose;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 相机品牌信息,用户可以不止一相机
 *
 * @author toutoumu
 */
@Entity
@Table(name = "t_Brand")
public class Brand {
    /** 主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    @Column(name = "brandId")
    private int brandId;
    /** 品牌名称 */
    @Expose
    @Column(length = 30)
    private String name;
    /** 备注 */
    @Expose
    @Column
    private String mark;

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

}
