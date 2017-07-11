package com.dsfy.entity;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.List;

/**
 * 租赁信息
 *
 * @author toutoumu
 */
@Entity
@Table(name = "t_RentalInfo")
public class RentalInfo {
    /** 审核状态 0:未知 1:未审核 2:审核失败 3:审核成功 */
    public static class Verify {// 就用小写算了大写看不清楚
        // 审核状态0:未审核 1:审核失败 2:审核成功
        /** 0:未知 */
        public static final int nullVal = 0;
        /** 未审核 */
        public static final int unVerify = 1;
        /** 审核失败 */
        public static final int verifyField = 2;
        /** 审核成功 */
        public static final int verify = 3;
    }

    /** 有还是没有 */
    public static class YesNo {
        public static final int nullVal = 0;
        /** 有 */
        public static final int yes = 1;
        /** 没有 */
        public static final int no = 2;
    }

    /** 锁定状态 0:未知,1:未锁定, 2:用户信息已经锁定 */
    public static class Locked {// 就用小写算了大写看不清楚
        /** 0:未知 */
        public static final int nullVal = 0;
        /** 未锁定 */
        public static final int unlocked = 1;
        /** 用户信息已经锁定 */
        public static final int locked = 2;
    }

    /** 删除状态 */
    public static class DeleteState {
        /** 空 */
        public static final int nullVal = 0;
        /** 未删除 */
        public static final int unDelete = 1;
        /** 已删除 */
        public static final int deleted = 2;
    }

    /** 是否自动接单 */
    public static class AutoAccept {
        /** 未知 */
        public static final int nullVal = 0;
        /** 自动接收 */
        public static final int yes = 1;
        /** 不自动接收 */
        public static final int no = 2;
    }

    /** 主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    @Column(name = "rentalId")
    private int rentalId;
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
    /** 用户名(租赁信息发布者) */

    /** 镜头ID */
    @Expose
    @Column
    private int lensId;
    @Expose
    @Column
    private String lensBrand;
    @Expose
    @Column
    private String lensModel;

    @Expose
    @Column
    private String name;
    /** 相机ID关联到器材表 */
    @Expose
    @Column
    private int cameraId;
    @Expose
    @Column
    /**封面照片*/
    private String cover;
    /** SN号 */
    @Expose
    @Column
    private String snNumber;
    /** 价格 单位 分 */
    @Expose
    @Column
    private double price;
    /** 押金 单位分 */
    @Expose
    @Column
    private double deposit;
    /** 器材编号 */
    @Expose
    @Column
    private String serialNumber;
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
    /** 最小租期 */
    @Expose
    @Column(length = 4)
    private int minRental = 1;
    /** 最大租期 */
    @Expose
    @Column(length = 4)
    private int maxRental = 0;
    /** 是否自动接单 1:是 2:否 */
    @Expose
    @Column(length = 1)
    private int autoAccept;
    @Expose
    @Column
    private long createTime;
    /** 购买日期 */
    @Expose
    @Column
    private long purchaseDate;
    /** 评分 */
    @Expose
    @Column(precision = 4, scale = 2)
    private double score;
    /** 经度 */
    @Expose
    @Column
    private String longitude = "0";
    /** 纬度 */
    @Expose
    @Column
    private String latitude = "0";
    /** 审核状态 0:未知 1:未审核 2:审核失败 3:审核成功 */
    @Expose
    @Column
    private int verify;
    /** 用户是否锁定 0:未知,1:未锁定,2:已经锁定 */
    @Expose
    @Column
    private int locked;
    /** 是否删除 0.未知,1.未删除,2.已删除 */
    @Expose
    @Column(name = "isDelete")
    private int deleted;
    /** 租赁次数 在租赁完成后更新 */
    @Expose
    @Column
    private int rentalCount;
    @Expose
    @Column
    private long responseTime;

    /** 审核失败原因 */
    @Expose
    @Column
    private String reason;
    /** 备注 */
    @Expose
    @Column
    private String mark;
    /*****************************以下是配件信息*******************************/
    /** 存储卡容量 */
    @Expose
    @Column
    private String memory;
    /** 电池数量 */
    @Expose
    @Column
    private int battery;
    /** 充电器 */
    @Expose
    @Column
    private int charger;
    /** 肩带 */
    @Expose
    @Column
    private int strap;
    /** 相机包 */
    @Expose
    @Column
    private int cameraBag;
    /** 读卡器 */
    @Expose
    @Column
    private int cardReader;
    /** 无线快门 */
    @Expose
    @Column
    private int wirelessShutter;
    /** 清洁工具 */
    @Expose
    @Column
    private int cleaningTool;
    /** 防水壳 */
    @Expose
    @Column
    private int waterproofShell;
    /** 扣具类 */
    @Expose
    @Column
    private int buckle;
    /** 遥控器 */
    @Expose
    @Column
    private int remoteControl;
    /** 飞行器 */
    @Expose
    @Column
    private int aircraft;

    /******************************************以下是镜头的***********************************/
    /** 遮光罩 */
    @Expose
    @Column
    private int lensHood;
    /** UV镜 */
    @Expose
    @Column
    private int uv;
    /** 镜头袋 */
    @Expose
    @Column
    private int lenBag;
    /** 偏光镜 */
    @Expose
    @Column
    private int polarizer;
    /** 减光镜 */
    @Expose
    @Column
    private int ndMirror;
    /** 灰渐变 */
    @Expose
    @Column
    private int grayGradient;

    /************************ 以下字段不会序列化到数据库 *************************/
    /** 相机类型(单反，微单，水下相机，卡片机) 用于传递查询条件 */
    /** 相机样张 */
    @Transient
    @Expose
    private List<ImageInfo> demos;
    @Transient
    @Expose
    private int category;
    /** 是否已经收藏 */
    @Transient
    @Expose
    private boolean isCollected;
    /** 租赁信息拒绝租赁时间 */
    @Transient
    @Expose
    private List<RejectTime> rejectTimes;
    /** 拒绝次数*/
    @Transient
    @Expose
    private int reject;

    public int getReject() {
        return reject;
    }

    public void setReject(int reject) {
        this.reject = reject;
    }

    public List<RejectTime> getRejectTimes() {
        return rejectTimes;
    }

    public void setRejectTimes(List<RejectTime> rejectTimes) {
        this.rejectTimes = rejectTimes;
    }

    public int getRentalId() {
        return rentalId;
    }

    public void setRentalId(int rentalId) {
        this.rentalId = rentalId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getLensBrand() {
        return lensBrand;
    }

    public void setLensBrand(String lensBrand) {
        this.lensBrand = lensBrand;
    }

    public String getLensModel() {
        return lensModel;
    }

    public void setLensModel(String lensModel) {
        this.lensModel = lensModel;
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

    public String getSnNumber() {
        return snNumber;
    }

    public void setSnNumber(String snNumber) {
        this.snNumber = snNumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
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

    public int getMinRental() {
        return minRental;
    }

    public void setMinRental(int minRental) {
        this.minRental = minRental;
    }

    public int getMaxRental() {
        return maxRental;
    }

    public void setMaxRental(int maxRental) {
        this.maxRental = maxRental;
    }

    public int getAutoAccept() {
        return autoAccept;
    }

    public void setAutoAccept(int autoAccept) {
        this.autoAccept = autoAccept;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(long purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public int getVerify() {
        return verify;
    }

    public void setVerify(int verify) {
        this.verify = verify;
    }

    public int getLocked() {
        return locked;
    }

    public void setLocked(int locked) {
        this.locked = locked;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public int getRentalCount() {
        return rentalCount;
    }

    public void setRentalCount(int rentalCount) {
        this.rentalCount = rentalCount;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    public int getCharger() {
        return charger;
    }

    public void setCharger(int charger) {
        this.charger = charger;
    }

    public int getStrap() {
        return strap;
    }

    public void setStrap(int strap) {
        this.strap = strap;
    }

    public int getCameraBag() {
        return cameraBag;
    }

    public void setCameraBag(int cameraBag) {
        this.cameraBag = cameraBag;
    }

    public int getCardReader() {
        return cardReader;
    }

    public void setCardReader(int cardReader) {
        this.cardReader = cardReader;
    }

    public int getWirelessShutter() {
        return wirelessShutter;
    }

    public void setWirelessShutter(int wirelessShutter) {
        this.wirelessShutter = wirelessShutter;
    }

    public int getCleaningTool() {
        return cleaningTool;
    }

    public void setCleaningTool(int cleaningTool) {
        this.cleaningTool = cleaningTool;
    }

    public int getWaterproofShell() {
        return waterproofShell;
    }

    public void setWaterproofShell(int waterproofShell) {
        this.waterproofShell = waterproofShell;
    }

    public int getBuckle() {
        return buckle;
    }

    public void setBuckle(int buckle) {
        this.buckle = buckle;
    }

    public int getRemoteControl() {
        return remoteControl;
    }

    public void setRemoteControl(int remoteControl) {
        this.remoteControl = remoteControl;
    }

    public int getAircraft() {
        return aircraft;
    }

    public void setAircraft(int aircraft) {
        this.aircraft = aircraft;
    }

    public int getLensHood() {
        return lensHood;
    }

    public void setLensHood(int lensHood) {
        this.lensHood = lensHood;
    }

    public int getUv() {
        return uv;
    }

    public void setUv(int uv) {
        this.uv = uv;
    }

    public int getLenBag() {
        return lenBag;
    }

    public void setLenBag(int lenBag) {
        this.lenBag = lenBag;
    }

    public int getPolarizer() {
        return polarizer;
    }

    public void setPolarizer(int polarizer) {
        this.polarizer = polarizer;
    }

    public int getNdMirror() {
        return ndMirror;
    }

    public void setNdMirror(int ndMirror) {
        this.ndMirror = ndMirror;
    }

    public int getGrayGradient() {
        return grayGradient;
    }

    public void setGrayGradient(int grayGradient) {
        this.grayGradient = grayGradient;
    }

    public List<ImageInfo> getDemos() {
        return demos;
    }

    public void setDemos(List<ImageInfo> demos) {
        this.demos = demos;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public boolean isCollected() {
        return isCollected;
    }

    public void setIsCollected(boolean isCollected) {
        this.isCollected = isCollected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RentalInfo that = (RentalInfo) o;

        return rentalId == that.rentalId;

    }

    @Override
    public int hashCode() {
        return rentalId;
    }
}
