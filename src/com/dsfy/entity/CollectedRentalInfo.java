package com.dsfy.entity;

import com.dsfy.entity.relation.PK_User_Rental;
import com.google.gson.annotations.Expose;

import javax.persistence.*;

/**
 * 收藏的租赁信息
 *
 * @author toutoumu
 */
@Entity
@Table(name = "t_Collected_RentalInfo")
@IdClass(PK_User_Rental.class)
public class CollectedRentalInfo {

    /** 外键用户id关联到{@link com.dsfy.entity.authority.SysmanUser} */
    @Id
    @Expose
    private int userId;
    /** 外键用户id关联到{@link RentalInfo} */
    @Id
    @Expose
    private int rentalId;
    /** 评分 */
    @Expose
    @Column
    private double score;
    @Expose
    @Column
    private int brandId;
    /** 品牌 */
    @Expose
    @Column
    private String brand;
    /** 相机ID */
    @Expose
    @Column
    private int cameraId;
    /** 镜头ID */
    @Expose
    @Column
    private int lensId;
    /** 型号 */
    @Expose
    @Column
    private String model;
    /** 封面照片 */
    @Expose
    @Column
    private String cover;

    /** 价格 单位 元 */
    @Expose
    @Column
    private double price;
    /** 租赁次数 在租赁完成后更新 */
    @Expose
    @Column
    private int rentalCount;
    /** 城市编码 */
    @Expose
    @Column
    private int cityId;
    /** 城市 */
    @Expose
    @Column
    private String city;
    /** 地址 */
    @Expose
    @Column
    private String address;
    /** 收藏时间 */
    @Expose
    @Column
    private long collectTime;
    /** 备注 */
    @Column
    @Expose
    private String remark;

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

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getCameraId() {
        return cameraId;
    }

    public void setCameraId(int cameraId) {
        this.cameraId = cameraId;
    }

    public int getLensId() {
        return lensId;
    }

    public void setLensId(int lensId) {
        this.lensId = lensId;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getRentalCount() {
        return rentalCount;
    }

    public void setRentalCount(int rentalCount) {
        this.rentalCount = rentalCount;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(long collectTime) {
        this.collectTime = collectTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CollectedRentalInfo that = (CollectedRentalInfo) o;

        if (userId != that.userId) return false;
        return rentalId == that.rentalId;

    }

    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + rentalId;
        return result;
    }
}
