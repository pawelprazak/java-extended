package com.bluecatcode.time;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Date;

/**
 * Ensures ISO 8601 date time format with UTC timezone
 */
public class ISO8601 {

    public static Date isoToDate(String date) {
        return isoToDateTime(date).toDate();
    }

    public static String dateToIso(Date date) {
        DateTime dateTime = new DateTime(date);
        return dateTimeToIso(dateTime);
    }

    public static DateTime isoToDateTime(String dateTime) {
        DateTimeFormatter parser = ISODateTimeFormat.dateTimeParser().withZoneUTC();
        return parser.parseDateTime(dateTime);
    }

    public static String dateTimeToIso(DateTime dateTime) {
        DateTimeFormatter formatter = ISODateTimeFormat.dateTimeNoMillis().withZoneUTC();
        return formatter.print(dateTime);
    }
}

