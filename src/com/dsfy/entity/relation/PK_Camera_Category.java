package com.dsfy.entity.relation;

import java.io.Serializable;

/**
 * 相机所属类别
 */
public class PK_Camera_Category implements Serializable {

    private static final long serialVersionUID = -890616727598171830L;

    private int categoryId;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + cameraId;
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
        PK_Camera_Category other = (PK_Camera_Category) obj;
        if (cameraId != other.cameraId)
            return false;
        if (categoryId != other.categoryId)
            return false;
        return true;
    }

}
