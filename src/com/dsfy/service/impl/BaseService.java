package com.dsfy.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import com.dsfy.entity.authority.BaseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dsfy.dao.IBaseDao;
import com.dsfy.dao.util.Pagination;
import com.dsfy.dao.util.QueryCondition;
import com.dsfy.service.IBaseService;

/**
 * 所有服务的实现必须继承此类,以获得基础操作支持
 *
 * @author 刘斌
 */
@Service("BaseService")
public class BaseService implements IBaseService {

    @Resource(name = "BaseDao")
    protected IBaseDao baseDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public <T> void delete(Class<T> clazz, Object id) {
        baseDao.delete(clazz, id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public <T> void delete(Class<T> clazz, Object[] ids) {
        baseDao.delete(clazz, ids);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int executeJpql(String jpql, Object... objects) {
        return baseDao.executeJpql(jpql, objects);
    }

    public <T> List<T> get(Class<T> clazz, List<QueryCondition> queryConditions, String orderBy, int currentPage, int pageSize) {
        return baseDao.get(clazz, queryConditions, orderBy, currentPage, pageSize);
    }

    public <T> List<T> get(Class<T> clazz, List<QueryCondition> queryConditions) {
        return baseDao.get(clazz, queryConditions);
    }

    public <T> List<T> get(Class<T> clazz, List<QueryCondition> queryConditions, String orderBy) {
        return baseDao.get(clazz, queryConditions, orderBy);
    }

    public <T> List<T> getAll(Class<T> clazz) {
        return baseDao.getAll(clazz);
    }

    public <T> T getById(Class<T> clazz, Object id) {
        return baseDao.getById(clazz, id);
    }

    public <T> List<T> getByIds(Class<T> clazz, Object[] ids) {
        return baseDao.getByIds(clazz, ids);
    }

    public <T> List<T> getByJpql(String jpql, Object... objects) {
        return baseDao.getByJpql(jpql, objects);
    }

    public <T> Pagination<T> getPagination(Class<T> clazz, List<QueryCondition> queryConditions, String orderBy, int currentPage,
                                           int pageSize) {
        return baseDao.getPagination(clazz, queryConditions, orderBy, currentPage, pageSize);
    }

    public <T> List<T> getPaginationJpql(int currentPage, int pageSize, String jpql, Object... objects) {
        return baseDao.getPageQuery(currentPage, pageSize, jpql, objects);
    }

    public long getRecordCount(Class<?> clazz, List<QueryCondition> queryConditions) {
        return baseDao.getRecordCount(clazz, queryConditions);
    }

    public long getRecordCount(Class<?> clazz, QueryCondition queryCondition) {
        List<QueryCondition> queryConditions = new ArrayList<>();
        queryConditions.add(queryCondition);
        return baseDao.getRecordCount(clazz, queryConditions);
    }

    public long getRecordCountByJpql(String jpql, Object... objects) {
        return baseDao.getRecordCountByJpql(jpql, objects);
    }

    public Object getSingleResult(Class<?> clazz, List<QueryCondition> queryConditions) {
        return baseDao.getSingleResult(clazz, queryConditions);
    }

    public Object getUniqueResultByJpql(String jpql, Object... objects) {
        return baseDao.getUniqueResultByJpql(jpql, objects);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void add(Object entity) {
        baseDao.save(entity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public <T> void batchSave(List<T> entitys) {
        baseDao.batchSave(entitys);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void update(Object entity) {
        baseDao.update(entity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int executeBySQL(String sql, Object... params) {
        return baseDao.executeBySQL(sql, params);
    }

    @Override
    public <T> List<T> get(Class<T> clazz, QueryCondition queryCondition) {
        List<QueryCondition> conditions = new ArrayList<QueryCondition>();
        conditions.add(queryCondition);
        return baseDao.get(clazz, conditions);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteLogicByIds(Class clazz, Object... ids) {
        StringBuilder ins = new StringBuilder();
        for (Object objects : ids) {
            ins.append("'").append(objects).append("',");
        }
        ins.append("'0'");
        String hql = "update " + clazz.getSimpleName() + " t set   t.deleteFlag = ?  where t.pid in (" + ins.toString() + ") ";
        baseDao.executeJpql(hql, BaseEntity.DeleteFlag.deleted);
    }

}
