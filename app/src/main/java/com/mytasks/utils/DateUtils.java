package com.mytasks.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by nimilpeethambaran on 8/25/15.
 */
public class DateUtils {
    private final static SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

    public static Date getDate(String date) throws ParseException {
        return format.parse(date);
    }

    public static String parseDate(Date date){
        return format.format(date);
    }
}
