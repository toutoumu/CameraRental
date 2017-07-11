package com.dsfy.controller;

import com.dsfy.dao.util.Pagination;
import com.dsfy.entity.http.JsonResponse;
import com.dsfy.entity.pay.Withdrawals;
import com.dsfy.service.IWithdrawalsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Withdrawals维护
 */
@Controller
@RequestMapping(value = "/WithdrawalsController", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
public class WithdrawalsController {
    @Resource(name = "WithdrawalsService")
    private IWithdrawalsService bannerService;

    /**
     * 添加Withdrawals
     *
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/unFinish.do")
    public JsonResponse unFinish(@RequestBody(required = false) Withdrawals withdrawals) throws IOException {
        if (withdrawals == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (withdrawals.getWithdrawalsId() == 0) {
            return JsonResponse.error("主键不能为空");
        }
        withdrawals = bannerService.getById(Withdrawals.class, withdrawals.getWithdrawalsId());
        if (withdrawals == null) {
            return JsonResponse.error("更新的提现信息不存在");
        }
        withdrawals.setState(Withdrawals.State.unFinish);
        bannerService.update(withdrawals);
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.setData("withdrawals", withdrawals);
        return jsonResponse;
    }

    /**
     * 添加Withdrawals
     *
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/finish.do")
    public JsonResponse finish(@RequestBody(required = false) Withdrawals withdrawals) throws IOException {
        if (withdrawals == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (withdrawals.getWithdrawalsId() == 0) {
            return JsonResponse.error("主键不能为空");
        }
        withdrawals = bannerService.getById(Withdrawals.class, withdrawals.getWithdrawalsId());
        if (withdrawals == null) {
            return JsonResponse.error("更新的提现信息不存在");
        }
        withdrawals.setState(Withdrawals.State.finished);
        bannerService.update(withdrawals);
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.setData("withdrawals", withdrawals);
        return jsonResponse;
    }

    /**
     * 删除Withdrawals
     *
     * @param withdrawals
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delete.do")
    public JsonResponse delete(@RequestBody(required = false) Withdrawals withdrawals) {
        if (withdrawals == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (withdrawals.getWithdrawalsId() == 0) {
            return JsonResponse.error("主键不能为空");
        }

        Withdrawals temp = bannerService.getById(Withdrawals.class, withdrawals.getWithdrawalsId());
        if (temp == null) {
            return JsonResponse.error("删除的退款信息不存在");
        }
        bannerService.delete(Withdrawals.class, withdrawals.getWithdrawalsId());
        return JsonResponse.success("删除成功");
    }

    @ResponseBody
    @RequestMapping(value = "/update.do")
    public JsonResponse update(@RequestBody(required = false) Withdrawals withdrawals) throws IOException {
        return JsonResponse.success("更新成功");
    }

    @ResponseBody
    @RequestMapping(value = "/getById.do")
    public JsonResponse getById(@RequestBody(required = false) Withdrawals withdrawals) {
        if (withdrawals == null || withdrawals.getWithdrawalsId() == 0) {
            return JsonResponse.error("查询条件不能为空");
        }
        withdrawals = bannerService.getById(Withdrawals.class, withdrawals.getWithdrawalsId());
        JsonResponse response = JsonResponse.success("查询成功");
        response.setData("withdrawals", withdrawals);
        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/query.do")
    public JsonResponse query(@RequestBody(required = false) Pagination<Withdrawals> withdrawals) {
        if (withdrawals == null || withdrawals.getData() == null) {
            return JsonResponse.error("请求参数不能为空");
        }
        if (withdrawals.getCurrentPage() == 0) {
            withdrawals.setCurrentPage(1);
        }
        if (withdrawals.getPageSize() == 0) {
            withdrawals.setPageSize(30);
        }
        Pagination<Withdrawals> pagination = bannerService.query(withdrawals.getData(), withdrawals.getCurrentPage(), withdrawals.getPageSize());

        JsonResponse response = JsonResponse.success("成功");
        response.setData("pagination", pagination);
        return response;
    }
}
