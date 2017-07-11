package com.dsfy.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dsfy.entity.http.JsonResponse;

/**
 * 全局异常处理
 * 
 * @author toutoumu
 */
@Controller
public class ErrorController {
	@RequestMapping(value = "/error.do", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public JsonResponse error(HttpServletRequest request) {
		JsonResponse jsonResponse = new JsonResponse();
		Exception ex = (Exception) request.getAttribute("exception");
		if (ex == null) {
			jsonResponse.setSuccess(false);
			jsonResponse.setMessage("未知错误");
			return jsonResponse;
		}
		ex.printStackTrace();
		jsonResponse.setSuccess(false);
		jsonResponse.setCode(1);
		jsonResponse.setMessage(ex.getMessage());
		return jsonResponse;
	}
}
