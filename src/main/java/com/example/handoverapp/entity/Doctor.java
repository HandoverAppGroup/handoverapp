package com.example.handoverapp.entity;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

// Doctors are embedded in the task table
@Embeddable
public class Doctor {
    @NotBlank(message = "Please provide name")
    private String name;
    private String grade;

    public Doctor() {
    }

    public Doctor(String name, String grade) {
        this.name = name;
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
