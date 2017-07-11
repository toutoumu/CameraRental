package com.dsfy.service.system;

import com.dsfy.entity.authority.SysmanResource;
import com.dsfy.service.IBaseService;

import java.util.List;

/**
 * @author alexgaoyh
 * @desc 用户后台资源资源权限表--RBAC权限管理service接口
 * @Fri Aug 08 13:29:41 CST 2014
 */
public interface ISysmanResourceService extends IBaseService {

    List<SysmanResource> getRootResourceList();

    void update(SysmanResource entity) throws Exception;
}
