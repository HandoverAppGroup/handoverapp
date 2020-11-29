package com.example.handoverapp.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PatientTest {

    @Test
    void testNewPatient() {
        Patient p = new Patient("123","Very ill","Ward A Bed 2");
        assertEquals(p.getMrn(),"123");
        assertEquals(p.getClinicalSummary(),"Very ill");
        assertEquals(p.getLocation(), "Ward A Bed 2");
    }
}