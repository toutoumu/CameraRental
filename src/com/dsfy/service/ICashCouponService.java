package com.dsfy.service;

import com.dsfy.dao.util.Pagination;
import com.dsfy.entity.pay.CashCoupon;

public interface ICashCouponService extends IBaseService {

    /**
     * 根据条件查询代金券
     * @param cashCoupon
     * @param currentPage
     * @param pageSize
     * @return
     */
    Pagination<CashCoupon> query(CashCoupon cashCoupon,int currentPage,int pageSize);
}
