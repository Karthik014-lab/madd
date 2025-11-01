package com.example.volunteerapp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtils {

    private static final String DATE_FORMAT = "MMM dd, yyyy";
    private static final String TIME_FORMAT = "hh:mm a";
    private static final String DATE_TIME_FORMAT = "MMM dd, yyyy hh:mm a";

    public static String formatDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    public static String formatTime(String time) {
        return time;
    }

    public static String formatDateTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    public static long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }

    public static boolean isEventUpcoming(long eventDate) {
        return eventDate > getCurrentTimestamp();
    }

    public static boolean isEventPast(long eventDate) {
        return eventDate < getCurrentTimestamp();
    }
}