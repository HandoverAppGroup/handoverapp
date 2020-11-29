package com.example.handoverapp.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    @Test
    void testNewTaskHasDateCreated() {
        Task t = new Task();
        assertNotNull(t.getDateCreated());
    }

}