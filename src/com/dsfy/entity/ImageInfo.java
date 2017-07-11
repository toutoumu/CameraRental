package com.dsfy.entity;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_ImageInfo")
public class ImageInfo {
    /** 图片类型 */
    public static class Category {
        /** 默认 */
        public static final int nullVal = 0;
        /** 相机样张 */
        public static final int cameraImage = 1;
        /** 镜头样张 */
        public static final int lensImage = 2;
        /** 用户图片 (头像,身份证照片等) */
        public static final int userImage = 3;
        /** 租赁信息图片 */
        public static final int rentalImage = 4;
        /** 订单纠纷图片 */
        public static final int disputeImage = 5;
        /** 订单相机交接时候的照片 */
        public static final int orderImage = 6;

    }

    /** 图片尺寸 */
    public static class Size {
        public static final int nullVal = 0;
        public static final int small = 1;
        public static final int max = 2;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    @Column(name = "imageId")
    private int imageId;
    /** 保存的文件名 */
    @Expose
    @Column
    private String fileName;
    /** 文件保存路径如 d:\\image\\data后面不带\\ */
    @Expose
    @Column
    private String saveDir;
    /** 全路径名称(带文件名) */
    @Expose
    @Column
    private String fullPath;
    /** 原始文件名 */
    @Expose
    @Column
    private String orgName;
    /** 宽度 */
    @Expose
    @Column
    private int width;
    /** 高度 */
    @Expose
    @Column
    private int height;
    /** URL路径 */
    @Expose
    @Column(length = 300)
    private String url;
    /** 上传时间 */
    @Expose
    @Column
    private long uploadDate = new Date().getTime();
    /** 外键 */
    @Expose
    @Column
    private String foreignKey;
    /** 图片类型 */
    @Expose
    @Column
    private int category;
    /** 大图还是小图 */
    private int size;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getForeignKey() {
        return foreignKey;
    }

    public void setForeignKey(String foreignKey) {
        this.foreignKey = foreignKey;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    /**
     * 保存的文件名
     *
     * @return
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * 保存的文件名
     *
     * @param fileName
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * 文件保存路径如 d:\\image\\data后面不带\\
     *
     * @return
     */
    public String getSaveDir() {
        return saveDir;
    }

    /**
     * 文件保存路径如 d:\\image\\data后面不带\\
     *
     * @param saveDir
     */
    public void setSaveDir(String saveDir) {
        this.saveDir = saveDir;
    }

    /**
     * 全路径名称(带文件名)
     *
     * @return
     */
    public String getFullPath() {
        return fullPath;
    }

    /**
     * 全路径名称(带文件名)
     *
     * @param fullPath
     */
    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    /**
     * 原始文件名
     *
     * @return
     */
    public String getOrgName() {
        return orgName;
    }

    /**
     * 原始文件名
     *
     * @param orgName
     */
    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(long uploadDate) {
        this.uploadDate = uploadDate;
    }
}