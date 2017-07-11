package com.dsfy.service;

import com.dsfy.dao.util.Pagination;
import com.dsfy.entity.pay.Withdrawals;

public interface IWithdrawalsService extends IBaseService {

    /**
     * 查询
     *
     * @param withdrawals
     * @return
     */
    Pagination<Withdrawals> query(Withdrawals withdrawals, int currentPage, int pageSize);
}
