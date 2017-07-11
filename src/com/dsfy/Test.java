package com.dsfy;

import java.util.Calendar;
import java.util.Date;

/**
 * 用来做测试用的
 */
public class Test {

    private static void setImageLayout(int rW, int rH, int maxWidth, int maxHeight) {
        int videoWidth = rW;
        int videoHeight = rH;
        if (rW > maxWidth || rH > maxHeight) {//只有宽高超过了才执行缩放
            double rR = (double) rW / (double) rH;//实际比例
            double radio = (double) maxWidth / (double) maxHeight;
            if (rR > radio) {//3:1 > 1:1 表示宽度超过限制,设置宽度为最大值,然后计算高度即可
                videoWidth = maxWidth;//设置宽度为最大宽度
                videoHeight = (int) (videoWidth / rR);//计算高度
            } else {//根据高度计算
                videoHeight = maxHeight;
                videoWidth = (int) (videoHeight * rR);
            }
        }
        System.out.print(rH);
    }

    public static void main(String[] args) {
        int alpha = (0x0fffffff & 0xff000000) >>> 24;
        System.out.println(1<<0);

        alpha = (0xffffffff & 0xff000000) >>> 24;
        System.out.println(1<<1);

        // "obtainTime": 1440408097858, -1
        // "returnTime": 1440411719038 -1
        //取+2 换+5
        //取 1 换 6
        /*Date date = new Date();
        date.setTime(1440408097858l);
        System.out.println(1440411719038l - 2 * 24 * 60 * 60 * 1000);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 2);
        System.out.print(calendar.getTimeInMillis());*/
    }
}
