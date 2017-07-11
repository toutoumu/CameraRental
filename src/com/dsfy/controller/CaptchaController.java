package com.dsfy.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dsfy.entity.Captcha;
import com.dsfy.entity.http.JsonResponse;
import com.dsfy.service.ICaptchaService;
import com.dsfy.service.IUserService;
import com.dsfy.util.ValidateUtil;

@Controller
@RequestMapping(value = "/CaptchaController", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
public class CaptchaController {
	@Resource(name = "CaptchaService")
	private ICaptchaService captchaService;
	@Resource(name = "UserService")
	private IUserService userService;

	/**
	 * 获取验证码
	 * 
	 * @param captcha
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getCaptcha.do", method = RequestMethod.POST)
	public JsonResponse getCaptcha(@RequestBody(required = false) Captcha captcha) {
		if (captcha == null || ValidateUtil.isEmpty(captcha.getPhone())) {
			return (JsonResponse.error("手机号不能为空"));
		}
		String phone = captcha.getPhone();
		if (ValidateUtil.isEmpty(phone) || !ValidateUtil.isMobile(phone)) {
			return (JsonResponse.error("手机号码格式不正确" + phone));
		}
		if (userService.getByUserName(phone) != null) {
			return (JsonResponse.error("手机号码" + phone + "已经被注册"));
		}
		captcha = captchaService.getCaptcha(phone);
		JsonResponse jsonResponse = JsonResponse.success("获取验证码成功");
		jsonResponse.setData("captcha", captcha);
		return (jsonResponse);
	}

	/**
	 * 验证验证码
	 * 
	 * @param captcha
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/validateCaptcha.do")
	public JsonResponse validateCaptcha(@RequestBody(required = false) Captcha captcha) {
		if (captchaService.validateCaptcha(captcha)) {
			return JsonResponse.success("验证通过");
		}
		return JsonResponse.error("验证码错误");
	}
}
