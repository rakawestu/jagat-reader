package com.github.rakawestu.jagatreader.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

import static org.joda.time.format.DateTimeFormat.forPattern;

/**
 * @author rakawm
 */
public class TimeUtil {
    public static Locale indo = new Locale("in", "ID");
    public static final DateTimeFormatter DATE_FORMATTER = forPattern("EEE, dd MMM YYYY HH:mm:ss Z");
    public static final String FORMATTER = "d MMMM YYYY";

    public static DateTime fromFeed(String strDatetime) {
        try {
            return DATE_FORMATTER.withOffsetParsed().parseDateTime(strDatetime);
        } catch (Exception ignored) {
            return DateTime.now();
        }
    }

    public static String getFormattedDate(DateTime dateTime) {
        return dateTime.toString(FORMATTER, indo);
    }
}
