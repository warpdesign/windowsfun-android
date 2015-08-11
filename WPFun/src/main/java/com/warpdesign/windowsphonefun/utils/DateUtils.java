package com.warpdesign.windowsphonefun.utils;

import com.warpdesign.windowsphonefun.R;
import com.warpdesign.windowsphonefun.WPFunApp;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by nicolas ramz on 26/03/14.
 */
public class DateUtils {
    static public String XMLDateFormatString = "EEE, dd MMM yyyy HH:mm:ss zzz";

    static public String formatDate(Date date) {
        Date today = new Date();
        long diff = today.getTime() - date.getTime();
        String dateStr;

        if (today.getDate() == date.getDate() && today.getMonth() == date.getMonth() && today.getYear() == date.getYear()) {
            long hours = date.getHours();
            long minutes = date.getMinutes();
            if (hours < 10) {
                dateStr = "0" + hours + ":";
            } else {
                dateStr = Long.toString(hours) + ":";
            }

            if (minutes < 10) {
                dateStr += "0" + Long.toString(minutes);
            } else {
                dateStr += Long.toString(minutes);
            }
            return dateStr;
        } else if (diff < 172800000 && today.getDay() != date.getDay()) {
            // < 2d
            return WPFunApp.getCustomAppContext().getResources().getString(R.string.date_yesterday);
        } else if (diff < 864000000) {
            // < 10d
            return date.getDate() + " " + DateUtils.getMonth(date.getMonth());
        } else {
            int month = date.getMonth() + 1;
            int day = date.getDate();

            if (day > 9) {
                dateStr = Integer.toString(day);
            } else {
                dateStr  = "0" + Integer.toString(day);
            }

            dateStr += "/";

            if (month < 10) {
                dateStr += "0" + Integer.toString(month);
            } else {
                dateStr += Integer.toString(month);
            }

            dateStr += '/' + Integer.toString(date.getYear() + 1900);

            return dateStr;
        }
    }

    static public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month];
    }

    static public long getTimeFromXMLString(String xml) {
        long time = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtils.XMLDateFormatString, Locale.ENGLISH);
        String match = xml.replaceAll("(?s:.*?<" + WPFunApp.BUILD_DATE +">(.*?)</" + WPFunApp.BUILD_DATE + ">.*)", "$1");

        try {
            time = dateFormat.parse(match).getTime();
        } catch(ParseException e) {
            time = -1;
        }
        return time;
    }
}