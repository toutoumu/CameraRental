package com.dsfy.entity.relation;

import java.io.Serializable;

/**
 * 镜头所属类别
 */
public class PK_Lens_Category implements Serializable {

    private static final long serialVersionUID = -890616727598171830L;

    private int categoryId;
    private int lensId;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getLensId() {
        return lensId;
    }

    public void setLensId(int lensId) {
        this.lensId = lensId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + lensId;
        result = prime * result + categoryId;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PK_Lens_Category other = (PK_Lens_Category) obj;
        if (lensId != other.lensId)
            return false;
        if (categoryId != other.categoryId)
            return false;
        return true;
    }

}
