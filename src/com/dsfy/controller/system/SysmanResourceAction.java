package com.dsfy.controller.system;

import com.dsfy.entity.authority.SysmanResource;
import com.dsfy.service.system.ISysmanResourceService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author alexgaoyh
 * @desc 用户后台资源资源权限表--RBAC权限管理action控制类
 */
@Controller
@RequestMapping(value = "admin/sysmanResource")
public class SysmanResourceAction extends BaseController<SysmanResource> {

    private static final Logger LOGGER = Logger.getLogger(SysmanResource.class);

    @Resource(name = "SysmanResourceService")
    ISysmanResourceService sysmanResourceService;

    @Override
    public void getData(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<SysmanResource> _temp = sysmanResourceService.getRootResourceList();
        List<Map<String, Object>> nodes = resourceToTree(_temp);
        this.renderJson(nodes, response);
    }

    /**
     * 转换为tree类型
     *
     * @param sysResourceList 资源list集合
     * @return
     */
    private List<Map<String, Object>> resourceToTree(List<SysmanResource> sysResourceList) {
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (SysmanResource r : sysResourceList) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("resourceType", r.getResourceType());
            item.put("name", r.getName());
            item.put("description", r.getDescription());
            item.put("orderNo", r.getOrderNo());
            item.put("parent.pid", r.getParent() == null ? "" : r.getParent().getPid());
            item.put("parent.name", r.getParent() == null ? "" : r.getParent().getName());
            if (r.getSubResource() != null && r.getSubResource().size() != 0) {
                item.put("children", this.resourceToTree(r.getSubResource()));
                item.put("leaf", false);
            } else {
                item.put("leaf", true);
            }
            item.put("href", r.getHref());
            item.put("pid", r.getPid());
            item.put("deleteFlag", r.getDeleteFlag());
            item.put("createTime", r.getCreateTime());

            data.add(item);
        }
        return data;
    }

}
