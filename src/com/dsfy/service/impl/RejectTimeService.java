package com.dsfy.service.impl;

import com.dsfy.dao.util.QueryCondition;
import com.dsfy.entity.RejectTime;
import com.dsfy.entity.http.JsonResponse;
import com.dsfy.exception.JsonException;
import com.dsfy.service.IBrandService;
import com.dsfy.service.IRejectTimeService;
import com.dsfy.util.ValidateUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service("RejectTimeService")
public class RejectTimeService extends BaseService implements IRejectTimeService {

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    @Deprecated
    public void add(RejectTime rejectTime) {
        if (rejectTime.getUserId() == 0) {
            throw new JsonException("用户ID不能为空");
        }
        if (rejectTime.getRentalId() == 0) {
            throw new JsonException("租赁信息ID不能为空");
        }
        if (rejectTime.getTimeStr() == 0 || rejectTime.getTime() == 0) {
            throw new JsonException("拒绝租机时间不能为空");
        }
        List<QueryCondition> conditions = new ArrayList<>();
        conditions.add(new QueryCondition("userId", QueryCondition.EQ, rejectTime.getUserId()));
        conditions.add(new QueryCondition("rentalId", QueryCondition.EQ, rejectTime.getRentalId()));
        conditions.add(new QueryCondition("timeStr", QueryCondition.EQ, rejectTime.getTimeStr()));
        // 时间必须在区间内
        conditions.add(new QueryCondition("time", QueryCondition.GE, ValidateUtil.dayOfStar(rejectTime.getTime())));
        conditions.add(new QueryCondition("time", QueryCondition.LE, ValidateUtil.dayOfEnd(rejectTime.getTime())));
        //查询是否存在
        long temp = baseDao.getRecordCount(RejectTime.class, conditions);
        if (temp > 0) {
            return;
        }
        baseDao.save(rejectTime);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void update(List<RejectTime> rejectTimes) {
        // 先删除对应的拒绝时间
        RejectTime rejectTime = new RejectTime();
        String hql = "delete from RejectTime where userId = " + rejectTime.getUserId();
        if (rejectTime.getRentalId() != 0) {//如果租赁信息ID不为0,那么删除的是租赁信息的
            hql += " and rentalId = " + rejectTime.getRentalId();
        } else {//如果租赁信息ID为零则删除,用户全局的拒绝租赁信息
            hql += " and rentalId = " + rejectTime.getRentalId();
        }
        // 删除过期的数据
        hql += " or time <= " + ValidateUtil.dayOfStar(new Date().getTime());
        baseDao.executeJpql(hql);
        // 保存新的
        baseDao.batchSave(rejectTimes);
        /*for (RejectTime time : rejectTimes) {
            baseDao.save(time);
        }*/
    }

    @Override
    public List<RejectTime> getUserRejectTime(int userId) {
        List<QueryCondition> conditions = new ArrayList<>();
        conditions.add(new QueryCondition("userId", QueryCondition.EQ, userId));
        conditions.add(new QueryCondition("time", QueryCondition.GE, ValidateUtil.dayOfStar(new Date().getTime())));//拒绝服务时间必须在当前日期之后,即(ValidateUtil.dayOfStar(new Date().getTime()))之后
        conditions.add(new QueryCondition("rentalId", QueryCondition.EQ, 0));//查询用户的拒绝服务时间,所以这里租赁信息ID为空
        return baseDao.get(RejectTime.class, conditions);
    }

    @Override
    public List<RejectTime> getRentalRejectTime(RejectTime rejectTime) {
        // 机器的拒绝租赁时间由 机器的[拒绝租赁时间]和[用户的拒绝租赁时]间构成,这个时间在当前日期开始时间之后即(ValidateUtil.dayOfStar(new Date().getTime()))之后
        String hql = "from RejectTime where time>=? and ((userId = ? and rentalId = 0) or rentalId = ?)";
        return getByJpql(hql, ValidateUtil.dayOfStar(new Date().getTime()), rejectTime.getUserId(), rejectTime.getRentalId());
    }

    @Override
    public long hasRejectTimeInRange(int userId, int rentalId, long startTime, long endTime) {
        // 查询时间段包含的租赁信息不可出租时间数
        List<QueryCondition> conditions = new ArrayList<>();
        conditions.add(new QueryCondition("userId", QueryCondition.EQ, userId));//用户ID
        conditions.add(new QueryCondition("rentalId", QueryCondition.EQ, rentalId));//租赁信息ID
        conditions.add(new QueryCondition("time", QueryCondition.GE, ValidateUtil.dayOfStar(new Date().getTime())));//拒绝服务时间必须在当前日期之后
        conditions.add(new QueryCondition("time", QueryCondition.GE, ValidateUtil.dayOfStar(startTime)));//在租赁开始时间之后
        conditions.add(new QueryCondition("time", QueryCondition.LE, ValidateUtil.dayOfEnd(endTime)));//在租赁结束时间之前
        long count = getRecordCount(RejectTime.class, conditions);
        if (count > 0) {
            return count;
        }
        // 用户的拒绝租赁时间是否在这个时间段中
        conditions.clear();
        conditions.add(new QueryCondition("userId", QueryCondition.EQ, userId));//用户ID
        conditions.add(new QueryCondition("rentalId", QueryCondition.EQ, 0));//租赁信息ID
        conditions.add(new QueryCondition("time", QueryCondition.GE, ValidateUtil.dayOfStar(new Date().getTime())));//拒绝服务时间必须在当前日期之后
        conditions.add(new QueryCondition("time", QueryCondition.GE, ValidateUtil.dayOfStar(startTime)));//在租赁开始时间之后
        conditions.add(new QueryCondition("time", QueryCondition.LE, ValidateUtil.dayOfEnd(endTime)));//在租赁结束时间之前
        return getRecordCount(RejectTime.class, conditions);
    }
}
