package com.dsfy.controller.system;

import com.dsfy.dao.util.Pagination;
import com.dsfy.dao.util.QueryCondition;
import com.dsfy.entity.authority.BaseEntity;
import com.dsfy.entity.http.Data;
import com.dsfy.entity.http.Result;
import com.dsfy.service.IBaseService;
import com.dsfy.util.GenericUtil;
import com.dsfy.util.JsonUtils;
import org.apache.commons.lang.ArrayUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


public abstract class BaseController<E extends BaseEntity> {

    @Resource(name = "BaseService")
    IBaseService baseService;

    private Class<E> entityClass;

    @SuppressWarnings("unchecked")
    public BaseController() {
        try {
            entityClass = GenericUtil.getActualClass(this.getClass(), 0);
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    /**
     * 后台list页面
     * 如请求地址为：   	http://localhost:8080/web/sysmanRole/list
     * 则返回的页面应该在    /web/WEB-INF/views/sysmanRole/list.jsp
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(ModelAndView model) {
        RequestMapping rm = this.getClass().getAnnotation(RequestMapping.class);
        String moduleName = "";
        if (rm != null) {
            String[] values = rm.value();
            if (ArrayUtils.isNotEmpty(values)) {
                moduleName = values[0];
            }
        }
        if (moduleName.endsWith("/")) {
            moduleName = moduleName.substring(0, moduleName.length() - 1);
        }

        model.setViewName("views/" + moduleName + "/list");
        model.addObject("moduleName", moduleName);
        return model;
    }

    /**
     * 后台页面渲染easyui-datagrid 方法
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "getData")
    @ResponseBody
    public void getData(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String page = request.getParameter("page");//easyui datagrid 分页 页号
        String rows = request.getParameter("rows");//easyui datagrid 分页 页数
        if (page.equals("0")) {
            page = "1";
        }
        List<QueryCondition> conditions = new ArrayList<>();
        conditions.add(new QueryCondition("deleteFlag", QueryCondition.EQ, BaseEntity.DeleteFlag.unDelete));
        Pagination<E> pagination = baseService.getPagination(entityClass, conditions, null, Integer.parseInt(page), Integer.parseInt(rows));

        Data<E> data = new Data<>();
        data.setRows(pagination.getRecordList());
        data.setTotal(pagination.getRecordList().size());

        response.setContentType("text/plain; charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write(JsonUtils.toJson(data));
        writer.flush();
    }

    /**
     * 过滤属性集合
     *
     * @param condition
     */
    protected void decorateCondition(DetachedCriteria condition) {
        condition.add(Expression.eq("deleteFlag", BaseEntity.DeleteFlag.unDelete));
    }

    /**
     * json 化
     *
     * @param data
     * @param response
     * @throws IOException
     */
    public void renderJson(Object data, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain; charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write(JsonUtils.toJson(data));
        writer.flush();
    }

    /**
     * 保存
     *
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "doSave")
    @ResponseBody
    public String doSave(HttpServletRequest request, HttpServletResponse response, E entity) throws IOException {
        Result result = null;
        try {
            beforeDoSave(request, entity);
            baseService.add(entity);
            afterDoSave(request, entity);
            result = new Result(true, "保存成功！");
        } catch (Exception e) {
            result = new Result(false, "保存失败！" + e.getMessage());
        } finally {
            this.renderJson(result, response);
        }

        return null;
    }

    /**
     * 调用保存方法之前进行的方法调用
     *
     * @param request
     * @param entity  对应实体信息
     * @throws Exception
     */
    protected void beforeDoSave(HttpServletRequest request, E entity) throws Exception {

    }

    /**
     * 电泳保存方法之后进行的方法调用
     *
     * @param request
     * @param entity  对应实体信息
     * @throws Exception
     */
    protected void afterDoSave(HttpServletRequest request, E entity) throws Exception {

    }

    /**
     * 更新
     *
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "doUpdate")
    @ResponseBody
    public String doUpdate(HttpServletRequest request, HttpServletResponse response, E entity) throws IOException {
        Result result = null;
        try {
            beforeDoUpdate(request, entity);
            baseService.update(entity);
            afterDoUpdate(request, entity);
            result = new Result(true, "更新成功！");
        } catch (Exception e) {
            result = new Result(false, "更新失败！" + e.getMessage());
        } finally {
            this.renderJson(result, response);
        }
        return null;
    }

    /**
     * 调用更新操作之前进行的操作
     *
     * @param request
     * @param entity
     * @throws Exception
     */
    protected void beforeDoUpdate(HttpServletRequest request, E entity) throws Exception {

    }

    /**
     * 调用更新操作之后进行的操作
     *
     * @param request
     * @param entity
     * @throws Exception
     */
    protected void afterDoUpdate(HttpServletRequest request, E entity) throws Exception {

    }

    /**
     * 删除
     *
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "logicDelete")
    @ResponseBody
    public String logicDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Result result = null;
        try {
            String pids = request.getParameter("pids");
            if (pids != null) {
                baseService.deleteLogicByIds(entityClass, pids.split("::"));
                result = new Result(true, "删除成功！");
            } else {
                result = new Result(false, "没有参数，非法操作！");
            }
        } catch (Exception e) {
            result = new Result(false, "更新失败！" + e.getMessage());
            e.printStackTrace();
        } finally {
            this.renderJson(result, response);
        }
        return null;
    }

    public Class<E> getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

}
