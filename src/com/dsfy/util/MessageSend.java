package com.dsfy.util;

import com.cloopen.rest.sdk.CCPRestSDK;
import com.dsfy.entity.Captcha;
import com.dsfy.exception.BusinessException;

import java.util.Date;
import java.util.HashMap;
import java.util.Set;

/**
 * 文件名: MessageSend.java</br>
 * 编写者: 刘斌</br>
 * 编写日期: 2014年9月12日</br>
 * 简要描述: 短信消息发送辅助类</br>
 * 密码:19891121li 用户名:18673507440
 * 最新 2452244@qq.com rei1985318
 * 组件列表:
 * ******************** 修改日志 **********************************
 * 修改人： 修改日期：
 * 修改内容：
 */
public class MessageSend {

	/*
     * private static final String serverIP = "sandboxapp.cloopen.com";
	 * private static final String port = "8883";
	 * private static final String accountSid = "8a48b5514767145d0147702bae7a0354";
	 * private static final String accountToken = "4bf49b49a88e4ab68af12f817afd6eb4";
	 * private static final String appId = "aaf98f8947d7c8680147ed2023291c50";
	 * private static final String templateId = "1";
	 */

    // 下面这个是生产环境用的正式发布时候使用
    /*
	 * private static final String serverIP = "app.cloopen.com";
	 * private static final String port = "8883";
	 * private static final String accountSid = "8a48b5514767145d0147702bae7a0354";
	 * private static final String accountToken = "4bf49b49a88e4ab68af12f817afd6eb4";
	 * private static final String appId = "aaf98f8947a0321a0147a1892cdb00b4";
	 * private static final String templateId = "3436";
	 */

    private static final String serverIP = "sandboxapp.cloopen.com";
    // 下面这个是生产环境用的正式发布时候使用
    // private static final String serverIP = "app.cloopen.com";

    private static final String port = "8883";

    private static final String accountSid = "8a48b5514e236232014e2ed303290b54";

    private static final String accountToken = "b6cc19dfaeca452f99ca4322d3c106ae";

    private static final String appId = "aaf98f894e2360b4014e2ed3e7d10aa9";

    /** 验证码模板ID */
    private static final String templateId = "1";
    //private static final String templateId = "32672";

    /**
     * 发送验证码，如果{@link Captcha}的code属性为0表示发送错误
     *
     * @param phone 电话号码"号码1,号码2等"
     * @param code  验证码
     * @return
     */
    public static Captcha sendSMS(String phone, String code) {
        Captcha captcha = new Captcha();
        captcha.setCreateTime(new Date().getTime());
        captcha.setPhone(phone);

        HashMap<String, Object> result = null;

        CCPRestSDK restAPI = new CCPRestSDK();
        restAPI.init(serverIP, port);// 初始化服务器地址和端口，格式如下，服务器地址不需要写https://
        // restAPI.setAccount("accountSid", "accountToken");// 初始化主帐号名称和主帐号令牌
        restAPI.setAccount(accountSid, accountToken);// 初始化主帐号名称和主帐号令牌
        // restAPI.setAppId("AppId");// 初始化应用ID
        restAPI.setAppId(appId);// 初始化应用ID
        // result = restAPI.sendTemplateSMS("号码1,号码2等", "模板Id", new String[] { "模板内容1", "模板内容2" });
        result = restAPI.sendTemplateSMS(phone, templateId, new String[]{String.valueOf(code)});

        System.out.println("SDKTestGetSubAccounts result=" + result);
        if ("000000".equals(result.get("statusCode"))) {
            // 正常返回输出data包体信息（map）
            @SuppressWarnings("unchecked")
            HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");
            Set<String> keySet = data.keySet();
            for (String key : keySet) {
                Object object = data.get(key);
                System.out.println(key + " = " + object);
            }
            captcha.setMessage("验证码发送成功");
            captcha.setCode(code);
            return captcha;
        } else {
            // 异常返回输出错误码和错误信息
            System.out.println("错误码=" + result.get("statusCode") + " 错误信息= " + result.get("statusMsg"));
            throw new BusinessException(result.get("statusMsg").toString());
        }
    }

    /**
     * 发送短信通知
     *
     * @param phone      电话号码（"号码1,号码2等"）
     * @param templateId 短信模版id
     * @param para       短信模版填充参数
     * @return
     */
    public static boolean sendSMSByTemplate(String phone, String templateId, String... para) {

        HashMap<String, Object> result = null;

        CCPRestSDK restAPI = new CCPRestSDK();
        restAPI.init(serverIP, port);// 初始化服务器地址和端口，格式如下，服务器地址不需要写https://
        // restAPI.setAccount("accountSid", "accountToken");// 初始化主帐号名称和主帐号令牌
        restAPI.setAccount(accountSid, accountToken);// 初始化主帐号名称和主帐号令牌
        // restAPI.setAppId("AppId");// 初始化应用ID
        restAPI.setAppId(appId);// 初始化应用ID
        // result = restAPI.sendTemplateSMS("号码1,号码2等", "模板Id", new String[] { "模板内容1", "模板内容2" });
        result = restAPI.sendTemplateSMS(phone, templateId, para);

        System.out.println("SDKTestGetSubAccounts result=" + result);
        if ("000000".equals(result.get("statusCode"))) {
            // 正常返回输出data包体信息（map）
            @SuppressWarnings("unchecked")
            HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");
            Set<String> keySet = data.keySet();
            for (String key : keySet) {
                Object object = data.get(key);
                System.out.println(key + " = " + object);
            }
            System.out.println("信息发送成功");
            return true;
        } else {
            // 异常返回输出错误码和错误信息
            System.out.println("错误码=" + result.get("statusCode") + " 错误信息= " + result.get("statusMsg"));
            return false;
        }
    }

    public static void main(String[] args) {
        sendSMS("13798227076", "123456");
    }
}
