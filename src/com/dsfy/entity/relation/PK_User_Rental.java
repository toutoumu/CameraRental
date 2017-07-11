package com.dsfy.entity.relation;

import java.io.Serializable;

/**
 * 我的收藏主键
 */
public class PK_User_Rental implements Serializable {

    private static final long serialVersionUID = -890616727598171830L;

    private int userId;
    private int rentalId;

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + rentalId;
        result = prime * result + userId;
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
        PK_User_Rental other = (PK_User_Rental) obj;
        if (rentalId != other.rentalId)
            return false;
        if (userId != other.userId)
            return false;
        return true;
    }

}
