package com.dsfy.entity.equipment;

import com.dsfy.entity.ImageInfo;
import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.List;

/**
 * 镜头信息
 * 最大光圈，最小光圈，最近对焦距离，焦距范围，最大放大倍率，镜头口径，卡口，光圈叶片数，防抖，重量，外观尺寸，市场价格
 *
 * @author toutoumu
 */
@Entity
@Table(name = "t_CameraLens")
public class CameraLens {
    /** 主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    @Column(name = "lensId")
    private int lensId;
    /** 外键用户id关联到{@link com.dsfy.entity.authority.SysmanUser} */
    @Expose
    @Column
    private int userId;
    /** 品牌ID(外键关联到品牌) */
    @Expose
    @Column
    private int brandId;
    /** 品牌名称 (佳能,富士等) */
    @Expose
    @Column
    private String brand;
    /** 型号名称 */
    @Expose
    @Column
    private String model;
    /** 最小光圈 */
    @Expose
    @Column
    private String minAperture;
    /** 最大光圈 */
    @Expose
    @Column
    private String maxAperture;
    /** 最近对焦距离 */
    @Expose
    @Column
    private String minFocusDistance;
    /** 焦距范围 */
    @Expose
    @Column
    private String focusRange;
    /** 最大放大倍率 */
    @Expose
    @Column
    private String maxMagnification;
    /** 镜头口径 */
    @Expose
    @Column
    private String lensAperture;
    /** 卡口类型 */
    @Expose
    @Column
    private String kaKou;
    /** 光圈叶片数 */
    @Expose
    @Column
    private String apertureNumber;
    /** 防抖类型 */
    @Expose
    @Column
    private String antiShake;
    /** 重量 */
    @Expose
    @Column
    private String weight;
    /** 尺寸 */
    @Expose
    @Column
    private String size;
    /** 市场价格 */
    @Expose
    @Column
    private String price;
    /** 押金 单位分 */
    @Expose
    @Column
    private double deposit;
    /** 备注 */
    @Expose
    @Column
    private String mark;
    /************************************************ 下面属性不序列化到数据库 ****************************************/
    /** 相机类型(单反，微单，水下相机，卡片机) */
    @Transient
    @Expose
    private int categoryId;
    /** 镜头样图 */
    @Transient
    @Expose
    private List<ImageInfo> demos;

    public List<ImageInfo> getDemos() {
        return demos;
    }

    public void setDemos(List<ImageInfo> demos) {
        this.demos = demos;
    }

    public int getLensId() {
        return lensId;
    }

    public void setLenseId(int lensId) {
        this.lensId = lensId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategory(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getMinAperture() {
        return minAperture;
    }

    public void setMinAperture(String minAperture) {
        this.minAperture = minAperture;
    }

    public String getMaxAperture() {
        return maxAperture;
    }

    public void setMaxAperture(String maxAperture) {
        this.maxAperture = maxAperture;
    }

    public String getMinFocusDistance() {
        return minFocusDistance;
    }

    public void setMinFocusDistance(String minFocusDistance) {
        this.minFocusDistance = minFocusDistance;
    }

    public String getFocusRange() {
        return focusRange;
    }

    public void setFocusRange(String focusRange) {
        this.focusRange = focusRange;
    }

    public String getMaxMagnification() {
        return maxMagnification;
    }

    public void setMaxMagnification(String maxMagnification) {
        this.maxMagnification = maxMagnification;
    }

    public String getLensAperture() {
        return lensAperture;
    }

    public void setLensAperture(String lensAperture) {
        this.lensAperture = lensAperture;
    }

    public String getKaKou() {
        return kaKou;
    }

    public void setKaKou(String kaKou) {
        this.kaKou = kaKou;
    }

    public String getApertureNumber() {
        return apertureNumber;
    }

    public void setApertureNumber(String apertureNumber) {
        this.apertureNumber = apertureNumber;
    }

    public String getAntiShake() {
        return antiShake;
    }

    public void setAntiShake(String antiShake) {
        this.antiShake = antiShake;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
}
