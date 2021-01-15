package com.example.handoverapp.controller;

import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DateUtilsTest {

    @Test
    void dateFromString() {
        // Given
        String dateString = "2020-09-10-11-12";
        // When
        Optional<Date> d = DateUtils.dateFromString(dateString);
        // Then
        assertTrue(d.isPresent());
        ZonedDateTime dt = d.get().toInstant().atZone(ZoneId.systemDefault());
        assertEquals(dt.getHour(),11);
        assertEquals(dt.getMinute(),12);
        assertEquals(dt.getYear(),2020);
        assertEquals(dt.getMonthValue(),9);
        assertEquals(dt.getDayOfMonth(),10);
    }
}