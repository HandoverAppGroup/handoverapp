package com.example.handoverapp.entity;
import javax.persistence.*;

@Entity
public class Patient  {

    // Unique id is generated automatically
    @Id
    private String mrn;
    // Increase the length limit as this field is often long
    @Column(length = 1000)
    private String clinicalSummary;
    private String location;

    public Patient() {
    }

    public Patient(String mrn, String clinicalSummary, String location) {
        this.mrn = mrn;
        this.clinicalSummary = clinicalSummary;
        this.location = location;
    }

    public String getMrn() {
        return mrn;
    }

    public void setMrn(String mrn) {
        this.mrn = mrn;
    }

    public String getClinicalSummary() {
        return clinicalSummary;
    }

    public void setClinicalSummary(String clinicalSummary) {
        this.clinicalSummary = clinicalSummary;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
