package com.dsfy.controller.system;

import com.dsfy.dao.util.Pagination;
import com.dsfy.dao.util.QueryCondition;
import com.dsfy.entity.authority.BaseEntity;
import com.dsfy.entity.authority.SysmanRole;
import com.dsfy.entity.authority.SysmanUser;
import com.dsfy.entity.http.Data;
import com.dsfy.entity.http.Result;
import com.dsfy.exception.JsonException;
import com.dsfy.service.system.ISysmanRoleService;
import com.dsfy.service.system.ISysmanUserService;
import com.dsfy.util.JsonUtils;
import com.dsfy.util.ValidateUtil;
import org.apache.log4j.Logger;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.spec.ECFieldF2m;
import java.util.ArrayList;
import java.util.List;

/**
 * @author alexgaoyh
 * @desc 用户后台登陆用户表--RBAC权限管理action控制类
 * @Fri Aug 08 14:25:29 CST 2014
 */
@Controller
@RequestMapping(value = "admin/sysmanUser")
public class SysmanUserAction extends BaseController<SysmanUser> {

    private static final Logger LOGGER = Logger.getLogger(SysmanUser.class);

    @Resource(name = "SysmanUserService")
    ISysmanUserService sysmanUserService;


    @Resource(name = "SysmanRoleService")
    private ISysmanRoleService sysmanRoleService;

    @Override
    public void getData(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String page = request.getParameter("page");//easyui datagrid 分页 页号
        String rows = request.getParameter("rows");//easyui datagrid 分页 页数
        if (page.equals("0")) {
            page = "1";
        }
        List<QueryCondition> conditions = new ArrayList<>();
        conditions.add(new QueryCondition("deleteFlag", QueryCondition.EQ, BaseEntity.DeleteFlag.unDelete));//没有删除的
        conditions.add(new QueryCondition("category", QueryCondition.EQ, SysmanUser.Category.manager));//只查询系统操作员,不查询普通用户
        Pagination<SysmanUser> pagination = baseService.getPagination(SysmanUser.class, conditions, null, Integer.parseInt(page), Integer.parseInt(rows));

        Data<SysmanUser> data = new Data<>();
        data.setRows(pagination.getRecordList());
        data.setTotal(pagination.getRecordList().size());

        response.setContentType("text/plain; charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write(JsonUtils.toJson(data));
        writer.flush();
    }

    /**
     * 重写list方法，在进入list页面的时候，将sysmanRoleList 加载到页面中
     */
    @Override
    public ModelAndView list(ModelAndView model) {
        List<SysmanRole> sysmanRoleList = sysmanRoleService.getAll(SysmanRole.class);
        model.addObject("sysmanRoleList", sysmanRoleList);
        return super.list(model);
    }

    @Override
    protected void beforeDoSave(HttpServletRequest request, SysmanUser entity) throws Exception {
        //加密密码
        Md5Hash md5Hash = new Md5Hash("admin");
        entity.setPassword(md5Hash.toHex());
        entity.setLocked(SysmanUser.Locked.unlocked);//未锁定
        entity.setVerify(SysmanUser.Verify.unVerify);//未认证
        entity.setCategory(SysmanUser.Category.manager);//系统操作员
        loadEntityRoles(request, entity);

        super.beforeDoSave(request, entity);
    }

    /**
     * 更新用户信息
     *
     * @param request
     * @param response
     * @param entity
     * @return
     * @throws IOException
     */
    @Override
    public String doUpdate(HttpServletRequest request, HttpServletResponse response, SysmanUser entity) throws IOException {
        if (entity == null || entity.getPid() == null || entity.getPid() == 0) {
            throw new JsonException("用户ID不能为空");
        }
        SysmanUser user = baseService.getById(SysmanUser.class, entity.getPid());
        if (user == null) {
            throw new JsonException("更新的用户不存在");
        }
        loadEntityRoles(request, user);//角色
        if (!ValidateUtil.isEmpty(entity.getUserName())) user.setUserName(entity.getUserName());//用户名
        if (!ValidateUtil.isEmpty(entity.getRealName())) user.setRealName(entity.getRealName());//真实姓名
        if (!ValidateUtil.isEmpty(entity.getEmail())) user.setEmail(entity.getEmail());//Email
        if (!ValidateUtil.isEmpty(entity.getPhone())) user.setPhone(entity.getPhone());//电话(手机)
        if (!ValidateUtil.isEmpty(entity.getPosition())) user.setPosition(entity.getPosition());//职务
        if (!ValidateUtil.isEmpty(entity.getPositonDesc())) user.setPositonDesc(entity.getPositonDesc());//职务描述
        if (entity.getLocked() == 0) user.setLocked(entity.getLocked());//是否禁用
        user.setCategory(SysmanUser.Category.manager);//用户类别不允许变

        Result result = null;
        try {
            baseService.update(user);
            result = new Result(true, "更新成功！");
        } catch (Exception e) {
            result = new Result(false, "更新失败！" + e.getMessage());
        } finally {
            this.renderJson(result, response);
        }
        return null;
    }


    /**
     * 向实体对象加载关联关系（用户包含的角色信息）
     *
     * @param request 请求参数
     * @param entity  form表单提交的实体对象
     */
    private void loadEntityRoles(HttpServletRequest request, SysmanUser entity) {
        String relRolePids = request.getParameter("relRolePids");
        if (relRolePids != null && !relRolePids.equals("")) {
            String[] idArr = relRolePids.split(",");
            List<SysmanRole> list = new ArrayList<SysmanRole>();
            for (String id : idArr) {
                SysmanRole _role = new SysmanRole();
                _role.setPid(Integer.parseInt(id));
                list.add(_role);
            }
            entity.setRoles(list);
        }
    }
}
