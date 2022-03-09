package com.example.vegeyuk.marketresto.utils;

import android.content.Context;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateHelper {

    private static final SimpleDateFormat sMemoShowDateFormat = new SimpleDateFormat(
            "M.d a h:m");

    private static final PrettyTime PRETTY_TIME = new PrettyTime(new Locale("in_ID"));

    public static String getGridDate(Context context, long time) {
        Date date = new Date(time);
        return PRETTY_TIME.format(date);
    }

    public static String getMemoDate(Context context, long time) {
        return sMemoShowDateFormat.format(new Date(time));
    }
}
