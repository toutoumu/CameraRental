package com.dsfy.controller;

import com.dsfy.controller.base.BaseController;
import com.dsfy.entity.RejectTime;
import com.dsfy.entity.http.JsonResponse;
import com.dsfy.service.IRejectTimeService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = "/RejectTimeController", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
public class RejectTimeController extends BaseController<RejectTime> {
    @Resource(name = "RejectTimeService")
    private IRejectTimeService rejectTimeService;

    /**
     * 更新用户或者租赁信息的拒绝租赁时间
     *
     * @param rejectTime
     * @return
     */
    @RequestMapping(value = "/saveOrUpdate.do")
    @ResponseBody
    public JsonResponse update(@RequestBody(required = false) List<RejectTime> rejectTime) {
        if (rejectTime == null || rejectTime.size() == 0) {
            return JsonResponse.error("请求参数不能为空");
        }
        // 此处不验证租赁信息ID,因为需要兼容用户的不可租赁时间
        for (RejectTime time : rejectTime) {
            if (time.getUserId() == 0) {
                return JsonResponse.error("用户ID不能为空");
            }
            if (time.getTimeStr() == 0 || time.getTime() == 0) {
                return JsonResponse.error("拒绝租机时间不能为空");
            }
        }
        rejectTimeService.update(rejectTime);
        JsonResponse jsonResponse = JsonResponse.success("保存成功");
        jsonResponse.setData("rejectTimes",rejectTime);
        return jsonResponse;
    }

    /**
     * 获取用户的拒绝服务时间,不包括租赁信息的拒绝服务时间
     *
     * @param rejectTime
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/userRejectTime.do")
    public JsonResponse getUserRejectTime(@RequestBody(required = false) RejectTime rejectTime) {
        if (rejectTime == null || rejectTime.getUserId() == 0) {
            return JsonResponse.error("用户ID不能为空");
        }
        List<RejectTime> list = rejectTimeService.getUserRejectTime(rejectTime.getUserId());
        JsonResponse response = JsonResponse.success("查询成功");
        response.setData("rejectTimes", list);
        return response;
    }

    /**
     * 获取租赁信息的拒绝服务时间,包括用户的拒绝服务时间
     *
     * @param rejectTime
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/RentalRejectTime.do")
    @RequiresRoles("administrator")
    public JsonResponse getRentalRejectTime(@RequestBody(required = false) RejectTime rejectTime) {
        if (rejectTime == null || rejectTime.getUserId() == 0) {
            return JsonResponse.error("用户ID不能为空");
        }
        if (rejectTime.getRentalId() == 0) {
            return JsonResponse.error("租赁信息ID不能为空");
        }
        List<RejectTime> list = rejectTimeService.getRentalRejectTime(rejectTime);
        JsonResponse response = JsonResponse.success("查询成功");
        response.setData("rejectTimes", list);
        return response;
    }

    @Override
    protected JsonResponse beforeAdd(HttpServletRequest request, RejectTime entity) {
        if (entity.getUserId() == 0) {
            return JsonResponse.error("用户ID不能为空");
        }
        /*if (entity.getRentalId() == 0) {
            return JsonResponse.error("租赁信息ID不能为空");
        }*/
        if (entity.getTimeStr() == 0 || entity.getTime() == 0) {
            return JsonResponse.error("拒绝租机时间不能为空");
        }

        RejectTime temp = (RejectTime) baseService.getUniqueResultByJpql("from RejectTime where rentalId = ?", entity.getRentalId());
        if (temp != null) {

        }
        return null;
    }
}
