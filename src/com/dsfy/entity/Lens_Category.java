package com.dsfy.entity;

import com.dsfy.entity.relation.PK_Lens_Category;
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
@Table(name = "T_lens_Category")
@IdClass(PK_Lens_Category.class)
public class Lens_Category {

    /** 镜头ID */
    @Id
    @Expose
    private int lensId;
    /** 类别ID */
    @Id
    @Expose
    private int categoryId;

    public int getLensId() {
        return lensId;
    }

    public void setLensId(int lensId) {
        this.lensId = lensId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

}
