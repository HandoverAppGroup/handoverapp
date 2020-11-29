package com.example.handoverapp.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

public class DateUtils {

    public static Date getYesterday() {
        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        return Date.from(yesterday.atStartOfDay(defaultZoneId).toInstant());
    }

    public static String stringFromDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-hh-mm");
        return df.format(date);
    }

    public static Optional<Date> dateFromString(String string) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-hh-mm");
        try {
            return Optional.of(df.parse(string));
        } catch (ParseException e) {
            return Optional.empty();
        }
    }

}
