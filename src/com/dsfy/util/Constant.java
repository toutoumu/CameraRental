package com.dsfy.util;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Properties;

public class Constant {
    /**
     * 极光推送的key
     */
    public static String appKey = null;
    /**
     * 极光推送的秘钥
     */
    public static String masterSecret = null;

    /**
     * 盛付通商户号
     */
    public static String merchantNo = null;
    /**
     * 盛付通商户key
     */
    public static String merchantKey = null;
    /**
     * 盛付通时间戳获取
     */
    public static String timestampurl = null;

    /**
     * 服务器域名
     */
    public static final String domain = "http://localhost:8080/CameraRental";
    //public static final String domain = "http://115.29.54.221:8080/CameraRental";
    /**
     * 退款回调
     */
    public static final String refundNotifyUrl = domain + "/OrderController/refundCallBack.do";

    static {
        // 加载配置文件
        Properties properties = null;
        InputStream inputStream = null;
        try {
            // 加载极光推送相关参数
            properties = new Properties();
            inputStream = Constant.class.getResourceAsStream("/jpush-api.properties");
            properties.load(inputStream);
            appKey = properties.getProperty("appKey");
            masterSecret = properties.getProperty("masterSecret");
            if (inputStream != null) {
                inputStream.close();
            }
            // 加载盛付通支付相关参数
            properties = new Properties();
            inputStream = Constant.class.getResourceAsStream("/pay.properties");
            properties.load(inputStream);
            merchantNo = properties.getProperty("merchantNo");
            timestampurl = properties.getProperty("timestampurl");
            merchantKey = properties.getProperty("merchantKey");
            if (inputStream != null) {
                inputStream.close();
            }

        } catch (IOException e) {
        }
    }

    public static void main(String[] args) throws IOException {
        //取+2 换+5
        //取 1 换 6
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 2);
        System.out.print(calendar.getTimeInMillis());
    }

    /**
     * 讲请求参数写入文件
     *
     * @param request
     * @param fileName
     * @throws IOException
     */
    public static void RequestToFile(HttpServletRequest request, String fileName) throws IOException {
        Enumeration<String> names = request.getParameterNames();
        StringBuilder builder = new StringBuilder();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            builder.append("private String " + name + "=" + request.getParameter(name) + ";\n");
        }
        File file = new File("d:\\" + fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        Writer writer = new FileWriter(file);
        writer.write(builder.toString());
        writer.close();
    }
}
