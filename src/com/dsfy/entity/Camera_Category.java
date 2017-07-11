package com.dsfy.entity;

import com.dsfy.entity.relation.PK_Camera_Category;
import com.google.gson.annotations.Expose;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * 相机和类别关联关系
 *
 * @author toutoumu
 */
@Entity
@Table(name = "T_Camera_Category")
@IdClass(PK_Camera_Category.class)
public class Camera_Category {

    /** 类别ID */
    @Id
    @Expose
    private int categoryId;
    /** 相机ID */
    @Id
    @Expose
    private int cameraId;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getCameraId() {
        return cameraId;
    }

    public void setCameraId(int cameraId) {
        this.cameraId = cameraId;
    }

}
