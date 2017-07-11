package com.dsfy.entity;

import com.google.gson.annotations.Expose;

import javax.persistence.*;

/**
 * 顶部轮播图片
 */
@Entity
@Table(name = "t_Banner")
public class Banner {
    public static class Visible {
        public static final int nullVal = 0;
        public static final int visibal = 1;
        public static final int gone = 2;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    @Column(name = "bannerId")
    private int bannerId;
    /** 标题 */
    @Expose
    @Column
    private String title;
    /** 图片 */
    @Expose
    @Column
    private String image;
    /** 链接 */
    @Expose
    @Column
    private String url;
    /** 内容 */
    @Expose
    @Column(length = 10000)
    private String content;
    /** 是否显示 */
    @Expose
    @Column
    private int visible;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getBannerId() {

        return bannerId;
    }

    public void setBannerId(int bannerId) {
        this.bannerId = bannerId;
    }
}