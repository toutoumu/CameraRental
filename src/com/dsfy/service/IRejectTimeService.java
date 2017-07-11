package com.dsfy.service;

import com.dsfy.entity.RejectTime;

import java.util.List;

public interface IRejectTimeService extends IBaseService {
    /**
     * 添加拒绝租赁时间
     *
     * @param rejectTime
     */
    void add(RejectTime rejectTime);

    /**
     * 更新拒绝租赁时间
     *
     * @param rejectTimes
     */
    void update(List<RejectTime> rejectTimes);

    /**
     * 获取用户拒绝租赁时间,查询条件中RentalID应该为0
     *
     * @param userId
     * @return
     */
    List<RejectTime> getUserRejectTime(int userId);

    /**
     * 获取租赁信息拒绝租赁时间(包括用户全局的不可租赁时间)
     *
     * @param rejectTime
     * @return
     */
    List<RejectTime> getRentalRejectTime(RejectTime rejectTime);

    /**
     * 传入的租赁时间段是否包含该租赁信息的不可租赁时间
     * 即相机是否在这个时间段出租
     *
     * @param userId
     * @param startTime
     * @param endTime
     * @return
     */
    long hasRejectTimeInRange(int userId, int rentalId, long startTime, long endTime);
}
