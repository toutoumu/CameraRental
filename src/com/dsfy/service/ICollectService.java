package com.dsfy.service;

import com.dsfy.dao.util.Pagination;
import com.dsfy.entity.CollectedRentalInfo;
import com.dsfy.entity.authority.SysmanUser;

import java.util.List;

public interface ICollectService extends IBaseService {
    void deCollect(int userId, int rentalId);

    /**
     * 获取用户收藏信息
     *
     * @param userId
     * @return
     */
    List<CollectedRentalInfo> getByUser(int userId);

    /**
     * 分页查询
     *
     * @param userId      用户ID
     * @param rentalId    租赁信息ID
     * @param currentPage 当前页码从1开始
     * @param pageSize    页大小
     * @return
     */
    Pagination<CollectedRentalInfo> page(int userId, int rentalId, int currentPage, int pageSize);

    /**
     * @param rentalId
     * @return
     */
    List<SysmanUser> getByRental(int rentalId);

    /**
     * 是否被收藏
     *
     * @param userId
     * @param rentalId
     * @return
     */
    boolean isCollect(int userId, int rentalId);

}
