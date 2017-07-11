package com.dsfy.controller.base;

import com.dsfy.dao.util.Pagination;
import com.dsfy.dao.util.QueryCondition;
import com.dsfy.entity.Category;
import com.dsfy.entity.Category.TreeData;
import com.dsfy.entity.authority.BaseEntity;
import com.dsfy.entity.http.JsonResponse;
import com.dsfy.service.IBaseService;
import com.dsfy.service.ICamera_CategoryService;
import com.dsfy.service.ICategoryService;
import com.dsfy.service.IUserService;
import com.dsfy.service.impl.BaseService;
import com.dsfy.util.GenericUtil;
import com.dsfy.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public abstract class BaseController<E extends BaseEntity> {

    @Resource(name = "BaseService")
    protected IBaseService baseService;

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
     * 添加
     *
     * @param obj
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/add.do")
    public JsonResponse add(HttpServletRequest request, HttpServletResponse response, @RequestBody(required = false) E obj) {
        if (obj == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        JsonResponse jsonResponse = beforeAdd(request, obj);
        if (jsonResponse != null) return jsonResponse;
        baseService.add(obj);
        jsonResponse = JsonResponse.success("添加成功");
        jsonResponse.setData("data", obj);
        return jsonResponse;
    }

    /**
     * 调用保存方法之前进行的方法调用
     *
     * @param request httpRequest
     * @param entity  实体对象
     * @return 如果验证失败返回失败信息, 否则返回null
     */
    protected JsonResponse beforeAdd(HttpServletRequest request, E entity) {
        return null;
    }

    /**
     * 调用物理删除方法之前进行的方法调用
     *
     * @param request httpRequest
     * @param entity  实体对象
     * @return 删除之前的验证, 如果验证失败返回失败信息, 否则返回null
     */
    protected JsonResponse beforeDelet(HttpServletRequest request, E entity) {
        return null;
    }

    /**
     * 物理删除
     *
     * @param obj 删除的对象
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delete.do")
    public JsonResponse delete(HttpServletRequest request, HttpServletResponse response, @RequestBody(required = false) BaseEntity obj) {
        if (obj == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (obj.getPid() == null || obj.getPid() == 0) {
            return JsonResponse.error("主键不能为空");
        }
        E temp = baseService.getById(entityClass, obj.getPid());
        if (temp != null) {
            beforeDelet(request, temp);
            baseService.delete(entityClass, obj.getPid());
            return JsonResponse.success("删除成功");
        }
        return JsonResponse.error("不存在的数据");
    }

    /**
     * 逻辑删除,设置删除标志位为删除
     *
     * @param obj
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/logicDelete.do")
    public JsonResponse logicDelete(HttpServletRequest request, HttpServletResponse response, @RequestBody(required = false) BaseEntity obj) {
        if (obj == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (obj.getPid() == null || obj.getPid() == 0) {
            return JsonResponse.error("主键不能为空");
        }
        BaseEntity temp = baseService.getById(entityClass, obj.getPid());
        if (temp != null) {
            baseService.deleteLogicByIds(entityClass, obj.getPid());
            return JsonResponse.success("删除成功");
        }
        return JsonResponse.error("不存在的数据");
    }


    /**
     * 根据ID查询
     *
     * @param obj
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getById.do")
    public JsonResponse getById(HttpServletRequest request, HttpServletResponse response, @RequestBody(required = false) BaseEntity obj) {
        if (obj == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (obj.getPid() == null || obj.getPid() == 0) {
            return JsonResponse.error("主键不能为空");
        }
        BaseEntity temp = baseService.getById(entityClass, obj.getPid());
        JsonResponse jsonResponse = JsonResponse.success("成功");
        jsonResponse.setData("data", temp);
        return jsonResponse;
    }

    /**
     * 分页查询所有
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getAll.do")
    public JsonResponse getAll(HttpServletRequest request, HttpServletResponse response, Pagination<Object> pagination) {
        List<QueryCondition> conditions = new ArrayList<>();
        conditions.add(new QueryCondition("deleteFlag", QueryCondition.EQ, BaseEntity.DeleteFlag.unDelete));//未删除
        Pagination<E> pagination1 = new BaseService().getPagination(entityClass, conditions, null, pagination.getCurrentPage(), pagination.getPageSize());
        JsonResponse jsonResponse = JsonResponse.success("查询成功");
        jsonResponse.setData("pagination", pagination1);
        return jsonResponse;
    }
}
