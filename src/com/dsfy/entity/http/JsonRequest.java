package com.dsfy.entity.http;

import java.lang.reflect.Type;
import java.util.*;

import com.dsfy.entity.authority.SysmanUser;
import com.dsfy.util.JsonUtils;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

/**
 * @author 斌
 *         请求的数据类型
 *         <pre>
 *         {
 *         header : {
 *         className : "className",
 *         method : "method"
 *         },
 *         parameter : {
 *         parameter1 : "para",
 *         parameter2 : "para"
 *         },
 *         data : {
 *         data1 : {},
 *         data2 : {},
 *         data3 : {}
 *         }
 *         }
 *         </pre>
 */
public class JsonRequest {

    @Expose
    private Map<String, String> parameter;
    @Expose
    private JsonRequestHeader header;
    @Expose
    private Map<String, Object> data;

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
     * 设置参数
     *
     * @param key
     * @param value
     */
    public void setParameter(String key, String value) {
        if (this.parameter == null) {
            parameter = new HashMap<String, String>();
        }
        this.parameter.put(key, value);
    }

    public JsonRequestHeader getHeader() {
        return header;
    }

    public void setHeader(JsonRequestHeader header) {
        this.header = header;
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

    public void setData(String key, Object data) {
        if (this.data == null) {
            this.data = new HashMap<String, Object>();
        }
        this.data.put(key, data);
    }

    public static void main(String[] args) {
        Date date = new Date();
        date.setDate(29);
        System.out.println(date.getTime());
        JsonRequest request = new JsonRequest();
        request.setParameter("asdf", "asdfasd");
        request.setData("abc", "asdfasd");
        request.setData("user", new SysmanUser());

        List<SysmanUser> users = new ArrayList<SysmanUser>();
        for (int i = 0; i < 10; i++) {
            SysmanUser user = new SysmanUser();
            user.setPassword("adsfasdf");
            users.add(user);
        }
        request.setData("users", users);

        String json = JsonUtils.toJson(new SysmanUser());

        System.out.println(json);

        request = new Gson().fromJson(json, new TypeToken<JsonRequest>() {
        }.getType());

        users = request.getData("users", new TypeToken<List<SysmanUser>>() {
        }.getType());

        System.out.println(users.size());
    }

}
