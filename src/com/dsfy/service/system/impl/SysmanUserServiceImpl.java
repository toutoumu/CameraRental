package com.dsfy.service.system.impl;

import com.dsfy.entity.authority.BaseEntity;
import com.dsfy.entity.authority.SysmanUser;
import com.dsfy.service.impl.BaseService;
import com.dsfy.service.system.ISysmanUserService;
import org.springframework.stereotype.Service;


/**
 * @author alexgaoyh
 * @desc 用户后台登陆用户表--RBAC权限管理service接口实现类
 * @Fri Aug 08 14:25:29 CST 2014
 */
@Service("SysmanUserService")
public class SysmanUserServiceImpl extends BaseService implements ISysmanUserService {

    @Override
    public SysmanUser findByName(String userName) {

        String hql = "from SysmanUser where userName = ? and deleteFlag=" + BaseEntity.DeleteFlag.unDelete;
        return (SysmanUser) getUniqueResultByJpql(hql, userName);
    }
}
