package com.example.handoverapp.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

public class DateUtils {

    public static Date getRecent() {
        Date today = new Date();
        LocalDateTime recentCutOff = LocalDateTime.ofInstant(today.toInstant(), ZoneId.systemDefault()).minusDays(2);
        return Date.from(recentCutOff.atZone(ZoneId.systemDefault()).toInstant());
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
