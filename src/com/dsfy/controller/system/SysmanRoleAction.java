package com.dsfy.controller.system;

import com.dsfy.entity.authority.SysmanResource;
import com.dsfy.entity.authority.SysmanRole;
import com.dsfy.entity.http.TreeNode;
import com.dsfy.service.system.ISysmanResourceService;
import com.dsfy.service.system.ISysmanRoleService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author alexgaoyh
 * @desc 用户后台登陆用户角色表--RBAC权限管理action控制类
 * @Fri Aug 08 14:27:52 CST 2014
 */
@Controller
@RequestMapping(value = "admin/sysmanRole")
public class SysmanRoleAction extends BaseController<SysmanRole> {

    private static final Logger LOGGER = Logger.getLogger(SysmanRole.class);
    @Resource(name = "SysmanRoleService")
    ISysmanRoleService sysmanRoleService;

    @Resource(name = "SysmanResourceService")
    ISysmanResourceService sysmanResourceService;
    /**
     * 获取到 系统资源 数据对应的树结构集合
     *
     * @param request  请求
     * @param response 响应
     * @throws Exception 异常
     */
    @RequestMapping(value = "/getResourceTreeNode", method = RequestMethod.GET)
    public void getResourceTreeNode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<SysmanResource> _temp = sysmanResourceService.getRootResourceList();
        List<TreeNode> nodes = resourceToTree(_temp);
        this.renderJson(nodes, response);
    }

    /**
     * 转换为tree类型
     *
     * @param sysResourceList 资源list集合
     * @return
     */
    private List<TreeNode> resourceToTree(List<SysmanResource> sysResourceList) {
        List<TreeNode> data = new ArrayList<TreeNode>();
        for (SysmanResource r : sysResourceList) {
            TreeNode item = new TreeNode();
            item.setId(r.getPid());
            item.setText(r.getName());
            item.setChildren((r.getSubResource() == null && r.getSubResource().size() == 0) ? null : resourceToTree(r.getSubResource()));
            data.add(item);
        }
        return data;
    }

    @Override
    protected void beforeDoSave(HttpServletRequest request, SysmanRole entity)
            throws Exception {

        loadEntity(request, entity);

        super.beforeDoSave(request, entity);
    }

    @Override
    protected void beforeDoUpdate(HttpServletRequest request, SysmanRole entity)
            throws Exception {

        loadEntity(request, entity);

        super.beforeDoUpdate(request, entity);
    }

    /**
     * 封装entity
     *
     * @param request 请求
     * @param entity  实体
     */
    private void loadEntity(HttpServletRequest request, SysmanRole entity) {

        String resourcePid = request.getParameter("resourceIds");
        if (resourcePid != null && !resourcePid.equals("")) {
            String[] idArr = resourcePid.split(",");

            List<SysmanResource> list = new ArrayList<SysmanResource>();
            for (String id : idArr) {
                SysmanResource _role = new SysmanResource();
                _role.setPid(Integer.parseInt(id));
                list.add(_role);
            }

            entity.setResource(list);
        }
    }
}
