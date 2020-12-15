package com.example.handoverapp.controller;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DateUtilsTest {

    @Test
    void stringFromDate() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm");
        String dateInString = "22-01-2015 10:20";
        Date date = sdf.parse(dateInString);
        String res = DateUtils.stringFromDate(date);
        assertEquals(res, "2015-01-22-10-20");
    }

    @Test
    void dateFromString() {
        String dateString = "2020-09-10-11-12";
        Optional<Date> d = DateUtils.dateFromString(dateString);
        assertTrue(d.isPresent());
        ZonedDateTime dt = d.get().toInstant().atZone(ZoneId.systemDefault());
        assertEquals(dt.getHour(),11);
        assertEquals(dt.getMinute(),12);
        assertEquals(dt.getYear(),2020);
        assertEquals(dt.getMonthValue(),9);
        assertEquals(dt.getDayOfMonth(),10);
    }
}