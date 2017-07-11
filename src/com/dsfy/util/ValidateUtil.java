package com.dsfy.util;

import sun.applet.Main;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateUtil {

    /**
     * 获取这一天的开始时间 如 2015-07-18 0:0:0
     *
     * @param time
     * @return
     */
    public static long dayOfStar(long time) {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.HOUR, -12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime().getTime();
    }

    /**
     * 获取这一天的结束时间 如 2015-07-18 23:59:59
     *
     * @param time
     * @return
     */
    public static long dayOfEnd(long time) {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.HOUR, 11);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime().getTime();
    }

    /**
     * 对字符串进行MD5取摘要
     *
     * @param str
     * @return
     */
    public static String md5(String str) {
        String pwd = null;
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            pwd = new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return pwd;
    }

    /**
     * 手机号验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    /**
     * 电话号码验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isPhone(String str) {
        Pattern p1 = null, p2 = null;
        Matcher m = null;
        boolean b = false;
        p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$"); // 验证带区号的
        p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$"); // 验证没有区号的
        if (str.length() > 9) {
            m = p1.matcher(str);
            b = m.matches();
        } else {
            m = p2.matcher(str);
            b = m.matches();
        }
        return b;
    }

    /**
     * 验证字符串是否为空</br> null "" "   "等
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str == null || "".equals(str) || str.trim().equals("")) {
            return true;
        }
        return false;
    }

    static void main(String[] a) {
        System.out.print(md5("abc"));
    }
}
