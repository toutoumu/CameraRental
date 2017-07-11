package com.dsfy.entity.equipment;

import com.dsfy.entity.ImageInfo;
import com.dsfy.entity.authority.SysmanUser;
import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.List;

/**
 * 相机机身信息
 * 画幅，像素，对焦点数，防抖，照片分辨率，视频分辨率，存储卡类型，感光度，液晶屏尺寸，液晶屏分辨率，快门速度，WIFI功能，GPS，连拍速度，闪光灯，重量，尺寸，电池型号，拍摄张数。
 *
 * @author toutoumu
 */
@Entity
@Table(name = "t_camera")
public class Camera {
    /** 主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    @Column(name = "cameraId")
    private int cameraId;
    /** 外键用户id关联到{@link SysmanUser} */
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
    /** 画幅 */
    @Expose
    @Column
    private String frame;
    /** 像素 */
    @Expose
    @Column
    private String pixel;
    /** 对焦点数 */
    @Expose
    @Column
    private String focusCount;
    /** 防抖 */
    @Expose
    @Column
    private String antiShake;
    /** 照片分辨率 */
    @Expose
    @Column
    private String pictureResolution;
    /** 视频分辨率 */
    @Expose
    @Column
    private String videoResolution;
    /** 存储卡类型 */
    @Expose
    @Column
    private String memoryCardType;
    /** 感光度 */
    @Expose
    @Column
    private String sensitivity;
    /** 液晶屏尺寸 */
    @Expose
    @Column
    private String screenSize;
    /** 液晶屏分辨率 */
    @Expose
    @Column
    private String screenResolution;
    /** 快门速度 */
    @Expose
    @Column
    private String shutterSpeed;
    /** WIFI功能 */
    @Expose
    @Column
    private String wifi;
    /** GPS定位功能 */
    @Expose
    @Column
    private String gps;
    /** 连拍速度 */
    @Expose
    @Column
    private String continuousShootingSpeed;
    /** 闪光灯 */
    @Expose
    @Column
    private String flashLamp;
    /** 重量 */
    @Expose
    @Column
    private String weight;
    /** 尺寸 */
    @Expose
    @Column
    private String size;
    /** 电池型号 */
    @Expose
    @Column
    private String batteryModel;
    /** 拍摄张数 */
    @Expose
    @Column
    private String shootingNumber;
    /** 价格 */
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
    /** 相机样张 */
    @Transient
    @Expose
    private List<ImageInfo> demos;

    public List<ImageInfo> getDemos() {
        return demos;
    }

    public void setDemos(List<ImageInfo> demos) {
        this.demos = demos;
    }

    public int getCameraId() {
        return cameraId;
    }

    public void setCameraId(int cameraId) {
        this.cameraId = cameraId;
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

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getFrame() {
        return frame;
    }

    public void setFrame(String frame) {
        this.frame = frame;
    }

    public String getPixel() {
        return pixel;
    }

    public void setPixel(String pixel) {
        this.pixel = pixel;
    }

    public String getFocusCount() {
        return focusCount;
    }

    public void setFocusCount(String focusCount) {
        this.focusCount = focusCount;
    }

    public String getAntiShake() {
        return antiShake;
    }

    public void setAntiShake(String antiShake) {
        this.antiShake = antiShake;
    }

    public String getPictureResolution() {
        return pictureResolution;
    }

    public void setPictureResolution(String pictureResolution) {
        this.pictureResolution = pictureResolution;
    }

    public String getVideoResolution() {
        return videoResolution;
    }

    public void setVideoResolution(String videoResolution) {
        this.videoResolution = videoResolution;
    }

    public String getMemoryCardType() {
        return memoryCardType;
    }

    public void setMemoryCardType(String memoryCardType) {
        this.memoryCardType = memoryCardType;
    }

    public String getSensitivity() {
        return sensitivity;
    }

    public void setSensitivity(String sensitivity) {
        this.sensitivity = sensitivity;
    }

    public String getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(String screenSize) {
        this.screenSize = screenSize;
    }

    public String getScreenResolution() {
        return screenResolution;
    }

    public void setScreenResolution(String screenResolution) {
        this.screenResolution = screenResolution;
    }

    public String getShutterSpeed() {
        return shutterSpeed;
    }

    public void setShutterSpeed(String shutterSpeed) {
        this.shutterSpeed = shutterSpeed;
    }

    public String getWifi() {
        return wifi;
    }

    public void setWifi(String wifi) {
        this.wifi = wifi;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public String getContinuousShootingSpeed() {
        return continuousShootingSpeed;
    }

    public void setContinuousShootingSpeed(String continuousShootingSpeed) {
        this.continuousShootingSpeed = continuousShootingSpeed;
    }

    public String getFlashLamp() {
        return flashLamp;
    }

    public void setFlashLamp(String flashLamp) {
        this.flashLamp = flashLamp;
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

    public String getBatteryModel() {
        return batteryModel;
    }

    public void setBatteryModel(String batteryModel) {
        this.batteryModel = batteryModel;
    }

    public String getShootingNumber() {
        return shootingNumber;
    }

    public void setShootingNumber(String shootingNumber) {
        this.shootingNumber = shootingNumber;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }
}
