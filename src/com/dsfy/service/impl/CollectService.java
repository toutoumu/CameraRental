package com.dsfy.service.impl;

import com.dsfy.dao.util.Pagination;
import com.dsfy.dao.util.QueryCondition;
import com.dsfy.entity.CollectedRentalInfo;
import com.dsfy.entity.authority.SysmanUser;
import com.dsfy.service.ICollectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("CollectService")
public class CollectService extends BaseService implements ICollectService {

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deCollect(int userId, int rentalId) {
        baseDao.executeJpql("delete from CollectedRentalInfo where userId=? and rentalId = ?", userId, rentalId);
    }

    @Override
    public List<CollectedRentalInfo> getByUser(int userId) {
        return baseDao.getByJpql("from CollectedRentalInfo where userId=?", userId);
    }

    @Override
    public Pagination<CollectedRentalInfo> page(int userId, int rentalId, int currentPage, int pageSize) {
        List<QueryCondition> conditions = new ArrayList<>();
        if (userId != 0) {
            conditions.add(new QueryCondition("userId", QueryCondition.EQ, userId));
        }
        if (rentalId != 0) {
            conditions.add(new QueryCondition("rentalId", QueryCondition.EQ, rentalId));
        }
        Pagination<CollectedRentalInfo> pagination = baseDao.getPagination(CollectedRentalInfo.class, conditions, null, currentPage, pageSize);
        return pagination;
    }

    @Override
    public List<SysmanUser> getByRental(int rentalId) {
        String jpql = "from SysmanUser  p where EXISTS ( select up.userId  from CollectedRentalInfo up where up.userId  = p.id and up.rentalId = ?)";
        return baseDao.getByJpql(jpql, rentalId);
    }

    @Override
    public boolean isCollect(int userId, int rentalId) {
        List<QueryCondition> conditions = new ArrayList<>();
        conditions.add(new QueryCondition("userId", QueryCondition.EQ, userId));
        conditions.add(new QueryCondition("rentalId", QueryCondition.EQ, rentalId));
        return baseDao.getRecordCount(CollectedRentalInfo.class, conditions) > 0;
    }

}
