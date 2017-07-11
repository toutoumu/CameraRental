package com.dsfy.service;

import com.dsfy.entity.Captcha;

public interface ICaptchaService extends IBaseService {
	/**
	 * 根据手机号码获取验证码
	 * @param phone
	 * @return
	 */
	Captcha getCaptcha(String phone);

	/**
	 * 验证验证码
	 * @param captcha
	 * @return
	 */
	boolean validateCaptcha(Captcha captcha);
}
