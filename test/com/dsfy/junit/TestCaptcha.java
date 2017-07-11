package com.dsfy.junit;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.dsfy.entity.Captcha;
import com.dsfy.entity.http.JsonRequest;
import com.dsfy.service.ICaptchaService;
import com.google.gson.Gson;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "classpath:applicationContext*.xml", "classpath:spring-mvc.xml" })
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = false)
public class TestCaptcha extends AbstractTransactionalJUnit4SpringContextTests
{

	@Resource(name = "CaptchaService")
	private ICaptchaService captchaService;

	private static final String phone = "15989348952";

	@Test
	/**
	 * 测试获取验证码
	 */
	public void getc()
	{
		captchaService.getCaptcha(phone);
	}

	/**
	 * 验证验证码
	 */
	public void setc()
	{
		Captcha captcha = new Captcha();
		captcha.setPhone(phone);
		captcha.setCode("777777");
		captchaService.validateCaptcha(captcha);
	}

	public static void main(String[] args)
	{

		JsonRequest request = new JsonRequest();
		request.setParameter("phone", "15989348952");
		Gson gson = new Gson();
		System.out.println(gson.toJson(request));
		/*
		 * Logger logger = Logger.getLogger(Car.class);
		 * // 记录debug级别的信息
		 * logger.debug("This is debug message.");
		 * // 记录info级别的信息
		 * logger.info("This is info message.");
		 * // 记录error级别的信息
		 * logger.error("This is error message.");
		 * logger.error(new NullPointerException("asfd"));
		 * logger.error("asf", new NullPointerException("asfd"));
		 */

	}

}
