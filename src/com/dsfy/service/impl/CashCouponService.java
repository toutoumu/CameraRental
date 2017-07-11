package com.dsfy.service.impl;

import com.dsfy.dao.util.Pagination;
import com.dsfy.dao.util.QueryCondition;
import com.dsfy.entity.pay.CashCoupon;
import com.dsfy.service.ICashCouponService;
import com.dsfy.util.ValidateUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("CashCouponService")
public class CashCouponService extends BaseService implements ICashCouponService {
    @Override
    public Pagination<CashCoupon> query(CashCoupon cashCoupon, int currentPage, int pageSize) {
        List<QueryCondition> conditions = new ArrayList<>();
        if (!ValidateUtil.isEmpty(cashCoupon.getOrderNumber())) {//订单编号
            conditions.add(new QueryCondition("orderNumber", QueryCondition.EQ, cashCoupon.getOrderNumber()));
        }
        if (!ValidateUtil.isEmpty(cashCoupon.getName())) {// 用户姓名
            conditions.add(new QueryCondition("name", QueryCondition.LK, cashCoupon.getName()));
        }
        if (!ValidateUtil.isEmpty(cashCoupon.getPhone())) {// 手机
            conditions.add(new QueryCondition("phone", QueryCondition.EQ, cashCoupon.getPhone()));
        }
        if (cashCoupon.getState() != CashCoupon.State.nullVal) {//状态
            conditions.add(new QueryCondition("state", QueryCondition.EQ, cashCoupon.getState()));
        }
        if (!ValidateUtil.isEmpty(cashCoupon.getUserName())) {//用户名
            conditions.add(new QueryCondition("userName", QueryCondition.EQ, cashCoupon.getUserName()));
        }
        return baseDao.getPagination(CashCoupon.class, conditions, null, currentPage, pageSize);
    }
}
