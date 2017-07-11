package com.dsfy.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dsfy.entity.http.JsonResponse;
import com.dsfy.entity.pay.TradeRecord;
import com.dsfy.service.ITradeRecordService;
import com.dsfy.util.ValidateUtil;

/**
 * 交易记录
 * 
 * @author dell
 */
@Controller
@RequestMapping(value = "/TradeRecordController", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
public class TradeRecordController {
	@Resource(name = "TradeRecordService")
	private ITradeRecordService tradeRecordService;

	@ResponseBody
	@RequestMapping(value = "/add.do")
	public JsonResponse add(@RequestBody(required = false) TradeRecord tradeRecord) {
		if (tradeRecord == null) {
			return JsonResponse.error("请求参数不能为空");
		}
		if (tradeRecord.getUserId() == 0) {
			return JsonResponse.error("用户ID不能为空");
		}
		if (tradeRecord.getType() == 0) {
			return JsonResponse.error("消费类型不能为空");
		}
		if (tradeRecord.getAmount() == 0) {
			return JsonResponse.error("记录金额不能为空");
		}
		if (ValidateUtil.isEmpty(tradeRecord.getOrderNumber())) {
			return JsonResponse.error("订单ID不能为空");
		}

		tradeRecord.setTime(new Date().getTime());
		tradeRecordService.add(tradeRecord);
		JsonResponse jsonResponse = JsonResponse.success("保存成功");
		jsonResponse.setData("tradeRecord", tradeRecord);
		return jsonResponse;
	}

	@ResponseBody
	@RequestMapping(value = "/delete.do")
	public JsonResponse delete(@RequestBody(required = false) TradeRecord tradeRecord) {
		if (tradeRecord == null) {
			return JsonResponse.error("请求参数不能为空");
		}
		if (tradeRecord.setTradeId() == 0) {
			return JsonResponse.error("主键不能为空");
		}
		TradeRecord temp = tradeRecordService.getById(TradeRecord.class, tradeRecord.setTradeId());
		if (temp != null) {
			temp.setDeleted(1);
			tradeRecordService.update(temp);
			return JsonResponse.success("删除成功");
		}
		return JsonResponse.error("删除的评论不存在");
	}

	@ResponseBody
	@RequestMapping(value = "/getByUser.do")
	public JsonResponse getByModel(@RequestBody(required = false) TradeRecord camera) {
		if (camera == null || camera.getUserId() == 0) {
			return JsonResponse.error("用户ID不能为空");
		}
		List<TradeRecord> list = tradeRecordService.getByUser(camera.getUserId());
		JsonResponse response = JsonResponse.success("成功");
		response.setData("tradeRecord", list);
		return response;
	}

	@ResponseBody
	@RequestMapping(value = "/getByOrder.do")
	public JsonResponse getByBrand(@RequestBody(required = false) TradeRecord tradeRecord) {
		if (tradeRecord == null || ValidateUtil.isEmpty(tradeRecord.getOrderNumber())) {
			return JsonResponse.error("订单ID不能为空");
		}
		List<TradeRecord> list = tradeRecordService.getByOrder(tradeRecord.getOrderNumber());
		JsonResponse response = JsonResponse.success("成功");
		response.setData("tradeRecord", list);
		return response;
	}
}
