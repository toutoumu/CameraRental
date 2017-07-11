package com.dsfy.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dsfy.entity.Captcha;
import com.dsfy.exception.BusinessException;
import com.dsfy.service.ICaptchaService;
import com.dsfy.util.MessageSend;
import com.dsfy.util.ValidateUtil;

@Service("CaptchaService")
public class CaptchaService extends BaseService implements ICaptchaService {

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Captcha getCaptcha(String phone) {
		if (StringUtils.isEmpty(phone) || !ValidateUtil.isMobile(phone)) {
			throw new BusinessException("手机号码格式不正确" + phone);
		}
		// 生成验证码
		int mobile_code = (int) ((Math.random() * 9 + 1) * 100000);
		// 发送验证码
		Captcha captcha = MessageSend.sendSMS(phone, "" + mobile_code);
		// 保存验证码到数据库
		if (captcha != null && !ValidateUtil.isEmpty(captcha.getCode())) {
			// 查询是否已经存在验证码,如果存在则更新时间和验证码
			Captcha savedCaptcha = (Captcha) baseDao.getUniqueResultByJpql("from Captcha where Phone = ?", captcha.getPhone());
			if (savedCaptcha != null) {
				savedCaptcha.setCreateTime(new Date().getTime());
				savedCaptcha.setCode(captcha.getCode());
				savedCaptcha.setMessage(captcha.getMessage());
				baseDao.update(savedCaptcha);
				return savedCaptcha;
			}
			// 如果不存在则保存进去
			captcha.setCreateTime(new Date().getTime());
			baseDao.save(captcha);
			return captcha;
		}
		// 返回验证码
		return captcha;
	}

	@Override
	public boolean validateCaptcha(Captcha captcha) {
		// 如果电话号码或者验证码为空,返回false
		if (captcha == null || ValidateUtil.isEmpty(captcha.getPhone()) || ValidateUtil.isEmpty(captcha.getCode())) {
			throw new BusinessException("手机号和验证码不能为空");
		}
		if (!ValidateUtil.isMobile(captcha.getPhone())) {
			throw new BusinessException("手机号码格式不正确" + captcha.getPhone());
		}
		// 获取保存的验证码,验证验证码是否正确,且时间在1分钟之内
		Captcha savedCaptcha = (Captcha) baseDao.getUniqueResultByJpql("from Captcha where Phone = ?", captcha.getPhone());
		if (savedCaptcha == null) {
			throw new BusinessException("验证码已过期,或未获取验证码,请重新获取验证码");
		}
		if (!savedCaptcha.getCode().equals(captcha.getCode())) {
			throw new BusinessException("验证码错误");
		}
		Calendar savedTime = Calendar.getInstance(Locale.getDefault());
		savedTime.setTime(new Date(savedCaptcha.getCreateTime()));
		savedTime.add(Calendar.MINUTE, 10);
		Calendar currentTime = Calendar.getInstance(Locale.getDefault());
		currentTime.setTime(new Date());
		// 如果保存时间加上一分钟在当前时间之后那么返回true
		if (!savedTime.after(currentTime)) {
			throw new BusinessException("验证码已过期,请重新获取验证码");
		}
		return true;
	}
}
