package com.dsfy.util;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

public class JspUtil {
	/**
	 * 生成 var xxx = xxx;的js变量
	 * @param key
	 * @param value
	 * @return
	 */
	public static String createJSParameter(String key, Object value) {
		if (value == null) {
			return "var " + key + " = null";
		}
		return "var " + key + "=" + JsonUtils.getGson().toJson(value) + ";";
	}

	/**
	 * 将请求参数输出成JavaScript的变量</br>
	 * 如:key1=value1 转换为 var key1 = 'value1';
	 * @param request
	 * @return
	 */
	public static String createRequestParameter(HttpServletRequest request) {
		Enumeration<String> names = request.getParameterNames();
		StringBuilder builder = new StringBuilder("");
		while (names.hasMoreElements()) {
			String key = (String) names.nextElement();
			String value = request.getParameter(key);
			if (value != null && !value.trim().equals("")) {
				builder.append("var ");
				builder.append(key);
				builder.append(" = '");
				builder.append(value);
				builder.append("';");
				builder.append("\n");
			}
		}
		return builder.toString();
	}
}
