package com.dsfy.service;

import com.dsfy.dao.util.Pagination;
import com.dsfy.entity.RentalInfo;

/**
 * 租赁信息维护
 *
 * @author toutoumu
 */
public interface IRentalService extends IBaseService {
    /**
     * 根据条件查询
     *
     * @param pagination
     * @return 租赁信息列表
     */
    Pagination<RentalInfo> query(Pagination<RentalInfo> pagination);

    /**
     * 分页搜索
     *
     * @param keys        关键字
     * @param currentPage 当前页
     * @param pageSize    每页数据条数
     * @return
     */
    Pagination<RentalInfo> search(String keys, int currentPage, int pageSize);

    /**
     * 租赁信息是否被用户收藏
     *
     * @param rentalId
     * @param userId
     * @return
     */
    boolean isCollected(int rentalId, int userId);

    /**
     * 根据用户地理位置获取数据
     *
     * @param longitude   经度
     * @param latitude    纬度
     * @param cityId      城市编码
     * @param pageSize    页大小
     * @param currentPage 当前页
     * @return
     */
    Pagination<RentalInfo> getByDistance(String longitude, String latitude, int cityId, int pageSize, int currentPage);

}
