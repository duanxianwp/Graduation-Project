package com.sys.utils;

import com.sys.exception.AppException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateParser;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {

    final static String timeRegex = "yyyy-MM-dd HH:mm:ss";

    /**
     * new Date(new Date().toString())  得到的时间会与预期相差14小时
     * @param date
     * @return
     */
    public static String formatTime(String date) {

        if (StringUtils.isBlank(date)) {
            return date;
        }
        Date d = new Date(date);
        d = DateUtils.addHours(d, -14);
        return DateFormatUtils.format(d.getTime(), timeRegex);
    }

    public static String formatTime(Date date) {

        if (date == null) {
            return null;
        }
        Date d = new Date(date.toString());
        d = DateUtils.addHours(d, -14);
        return DateFormatUtils.format(d.getTime(), timeRegex);
    }
}
