package com.dsfy.service.system;

import com.dsfy.entity.authority.SysmanUser;
import com.dsfy.service.IBaseService;

/**
 * @author alexgaoyh
 * @desc 用户后台登陆用户表--RBAC权限管理service接口
 * @Fri Aug 08 14:25:29 CST 2014
 */
public interface ISysmanUserService extends IBaseService {

    /**
     * 根据用户名查找用户
     *
     * @param userName
     * @return
     */
    SysmanUser findByName(String userName);
}
