package com.dsfy.service.system.impl;

import java.util.List;

import com.dsfy.entity.authority.BaseEntity;
import com.dsfy.entity.authority.SysmanResource;
import com.dsfy.service.impl.BaseService;
import com.dsfy.service.system.ISysmanResourceService;
import org.springframework.stereotype.Service;

/**
 * @author alexgaoyh
 * @desc 用户后台资源资源权限表--RBAC权限管理service接口实现类
 * @Fri Aug 08 13:29:41 CST 2014
 */
@Service("SysmanResourceService")
public class SysmanResourceServiceImpl extends BaseService implements ISysmanResourceService {

    @Override
    public List<SysmanResource> getRootResourceList() {
        String hql = "from SysmanResource where deleteFlag=" + BaseEntity.DeleteFlag.unDelete + " and parent is null ";
        return getByJpql(hql);
    }

    @Override
    public void update(SysmanResource entity) throws Exception {

        checkErrorMove(entity, entity.getParent().getPid());

        super.update(entity);
    }

    /**
     * update操作，移动错误时抛出异常信息
     *
     * @param entity
     * @param parentId
     */
    private void checkErrorMove(SysmanResource entity, Integer parentId) {
        if (entity.getParent().getPid() == null) {
            throw new RuntimeException("顶级菜单不允许操作");
        } else {
            if (entity.getPid() == parentId) {
                throw new RuntimeException("父级知识点不能是自己！");
            } else {
                if (entity != null && entity.getSubResource() != null) {
                    for (SysmanResource kk : entity.getSubResource()) {

                        if (parentId == kk.getPid()) {
                            throw new RuntimeException("父级知识点不能是自己的子知识点！");
                        } else {
                            checkErrorMove(kk, parentId);
                        }
                    }
                }
            }
        }
    }


}
