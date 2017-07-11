package com.dsfy.controller;

import com.dsfy.entity.http.JsonResponse;
import com.dsfy.entity.pay.Order;
import com.dsfy.entity.pay.OrderDetail;
import com.dsfy.service.IOrderDetailService;
import com.dsfy.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 交易处理流程记录
 */
@Controller
@RequestMapping(value = "/OrderDetailController", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
public class OrderDetailController {
    @Resource(name = "OrderDetailService")
    private IOrderDetailService detailService;

    /**
     * 添加OrderDetail
     *
     * @param detail
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/add.do")
    public JsonResponse add(@RequestBody(required = false) OrderDetail detail) {
        if (detail == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (detail.getUserId() == 0) {
            return JsonResponse.error("用户ID不能为空");
        }
        if (ValidateUtil.isEmpty(detail.getName())) {
            return JsonResponse.error("用户名称不能为空");
        }
        detailService.add(detail);
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.setData("detail", detail);
        return jsonResponse;
    }

    /**
     * 删除OrderDetail
     *
     * @param detail
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delete.do")
    public JsonResponse delete(@RequestBody(required = false) OrderDetail detail) {
        if (detail == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (detail.getDetailId() == 0) {
            return JsonResponse.error("主键不能为空");
        }

        OrderDetail temp = detailService.getById(OrderDetail.class, detail.getDetailId());
        if (temp != null) {
            detailService.delete(OrderDetail.class, detail.getDetailId());
            return JsonResponse.success("删除成功");
        }
        return JsonResponse.error("删除的OrderDetail不存在");
    }

    @ResponseBody
    @RequestMapping(value = "/update.do")
    public JsonResponse update(@RequestBody(required = false) OrderDetail detail) {
        if (detail == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (detail.getDetailId() == 0) {
            return JsonResponse.error("主键不能为空");
        }
        if (detail.getUserId() == 0) {
            return JsonResponse.error("用户ID不能为空");
        }
        if (ValidateUtil.isEmpty(detail.getName())) {
            return JsonResponse.error("用户名称不能为空");
        }

        OrderDetail temp = detailService.getById(OrderDetail.class, detail.getDetailId());
        if (temp != null) {
            detailService.update(detail);
            return JsonResponse.success("更新成功");
        }
        return JsonResponse.error("更新的OrderDetail不存在");
    }

    @ResponseBody
    @RequestMapping(value = "/getById.do")
    public JsonResponse getById(@RequestBody(required = false) OrderDetail detail) {
        if (detail == null || detail.getDetailId() == 0) {
            return JsonResponse.error("查询条件不能为空");
        }
        detail = detailService.getById(OrderDetail.class, detail.getDetailId());
        JsonResponse response = JsonResponse.success("成功");
        response.setData("detail", detail);
        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/getByOrder.do")
    public JsonResponse getByOrder(@RequestBody(required = false) Order order) {
        if (order == null) {
            return JsonResponse.error("查询条件不能为空");
        }
        if (!ValidateUtil.isEmpty(order.getOrderNumber())) {
            return JsonResponse.error("订单编号不能为空");
        }
        order = detailService.getById(Order.class, order.getOrderNumber());
        JsonResponse response = JsonResponse.success("成功");
        response.setData("detail", order);
        return response;
    }
}
