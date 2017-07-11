package com.dsfy.entity.http;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.dsfy.util.JsonUtils;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

/**
 * @author 斌
 *         返回的数据结构如下
 *         <pre>
 *                 {
 *                 header : {
 *                 isSuccess : "className",
 *                 message : "method"
 *                 },
 *                 parameter : {
 *                 parameter1 : "para",
 *                 parameter2 : "para"
 *                 },
 *                 data : {
 *                 data1 : {},
 *                 data2 : {},
 *                 data3 : {}
 *                 }
 *                 }
 *                 </pre>
 */
public class JsonResponse {

    /**
     * 响应头
     */
    @Expose
    private JsonResponseHeader header = new JsonResponseHeader();

    /**
     * 响应参数
     */
    @Expose
    private Map<String, String> parameter = null;

    /**
     * 响应实体
     */
    @Expose
    private Map<String, Object> data = null;

    /**
     * 获取响应头
     *
     * @return
     */
    public JsonResponseHeader getHeader() {
        return this.header;
    }

    /**
     * 添加返回参数
     *
     * @param key
     * @param value
     */
    public void setData(String key, Object value) {
        if (this.data == null) {
            this.data = new HashMap<String, Object>();
        }
        this.data.put(key, value);
    }

    /**
     * 获取实体类型数据
     *
     * @param key    前端调用时设置成什么名字这里就是什么名字
     * @param claszz </br>
     *               注意</br>
     *               1.如果是实体类如User对象直接传User.class</br>
     *               2.如果是List&lt;User&gt;类型那么必须传new TypeToken &lt;List&lt;User&gt;&gt;() {}.getType()
     * @return
     */
    public <T> T getData(String key, Type claszz) {
        if (this.data == null) {
            return null;
        }
        Object value = this.data.get(key);
        Gson gson = JsonUtils.getGson();
        return gson.fromJson(gson.toJson(value), claszz);
    }

    /**
     * 添加响应参数
     *
     * @param key
     * @param value
     */
    public void setParameter(String key, String value) {
        if (this.parameter == null) {
            this.parameter = new HashMap<String, String>();
        }
        this.parameter.put(key, value);
    }

    /**
     * 获取参数
     *
     * @param key
     * @return
     */
    public String getParameter(String key) {
        if (parameter == null) {
            return null;
        }
        if (parameter.containsKey(key)) {
            return parameter.get(key);
        }
        return null;
    }

    /**
     * 是指服务调用是否成功
     *
     * @param b
     */
    public void setSuccess(boolean b) {
        this.header.setSuccess(b);
    }

    /**
     * 设置响应消息
     *
     * @param message
     */
    public void setMessage(String message) {
        this.header.setMessage(message);
    }

    /**
     * 设置响应码</br>
     * 302重定向
     *
     * @param code
     */
    public void setCode(int code) {
        this.header.setCode(code);
    }

    /**
     * 设置重定向位置
     *
     * @param location
     */
    public void setLocation(String location) {
        this.header.setLocation(location);
    }

    /** 构造错误信息 */
    public static JsonResponse error(String message) {
        JsonResponse response = new JsonResponse();
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }

    /** 构造成功信息 */
    public static JsonResponse success(String message) {
        JsonResponse response = new JsonResponse();
        response.setSuccess(true);
        response.setMessage(message);
        return response;
    }
}
