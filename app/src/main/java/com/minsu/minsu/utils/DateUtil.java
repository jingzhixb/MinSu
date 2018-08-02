package com.minsu.minsu.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lilei on 2017/8/29.
 */

public class DateUtil {
    public static String toDate(long number) {
        //时间戳转化为Sting或Date
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String d = format.format(number);
        System.out.println("Format To String(Date):" + d);
        return d;
    }


    public static String nowTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        return str;
    }

    public static Date getDate(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date adf = null;
        try {
            adf = sdf.parse(time);
            return adf;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

    }
}
