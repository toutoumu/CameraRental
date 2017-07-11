package com.dsfy.service.impl;

import com.dsfy.dao.util.Pagination;
import com.dsfy.dao.util.QueryCondition;
import com.dsfy.entity.authority.SysmanUser;
import com.dsfy.entity.pay.Withdrawals;
import com.dsfy.service.IBannerService;
import com.dsfy.service.IBrandService;
import com.dsfy.service.IWithdrawalsService;
import com.dsfy.util.ValidateUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("WithdrawalsService")
public class WithdrawalsService extends BaseService implements IWithdrawalsService {
    /**
     * @param withdrawals
     * @param currentPage 当前页
     * @param pageSize    页面大小
     * @return
     */
    @Override
    public Pagination<Withdrawals> query(Withdrawals withdrawals, int currentPage, int pageSize) {

        List<QueryCondition> conditions = new ArrayList<>();

        if (withdrawals.getUserId() != 0) {
            conditions.add(new QueryCondition("userId", QueryCondition.EQ, withdrawals.getUserId()));
        }
        if (withdrawals.getState() != 0) {
            conditions.add(new QueryCondition("state", QueryCondition.EQ, withdrawals.getState()));
        }
        if (!ValidateUtil.isEmpty(withdrawals.getUserName())) {//账号
            conditions.add(new QueryCondition("userName", QueryCondition.EQ, withdrawals.getUserName()));
        }
        if (!ValidateUtil.isEmpty(withdrawals.getPhone())) {//手机号
            conditions.add( new QueryCondition("phone", QueryCondition.EQ, withdrawals.getPhone()));
        }
        if (!ValidateUtil.isEmpty(withdrawals.getIdCard())) {//身份证
            conditions.add(new QueryCondition("idCard", QueryCondition.EQ, withdrawals.getIdCard()));
        }
        if (!ValidateUtil.isEmpty(withdrawals.getRealName())) {//姓名
            conditions.add(new QueryCondition("realName", QueryCondition.EQ, withdrawals.getRealName()));
        }

        return getPagination(Withdrawals.class, conditions, null, currentPage, pageSize);
    }
}
