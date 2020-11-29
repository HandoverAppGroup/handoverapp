package com.example.handoverapp.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoctorTest {

    @Test
    void testNewDoctor() {
        Doctor d = new Doctor("Dr Stephens","A");
        assertEquals(d.getGrade(),"A");
        assertEquals(d.getName(), "Dr Stephens");
    }
}