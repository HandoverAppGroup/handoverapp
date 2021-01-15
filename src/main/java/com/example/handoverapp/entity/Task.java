package com.example.handoverapp.entity;

import javax.persistence.*;
import java.util.Date;


@Entity
public class Task {

    // Unique id is generated automatically
    @Id
    @GeneratedValue
    private long id;

    private boolean completed = false;
    private final Date dateCreated = new Date();
    private Date dateCompleted;

    // Increase the length limit as this field is often long
    @Column(length = 1000)
    private String description;
    private String gradeRequired;

    @ManyToOne(cascade=CascadeType.PERSIST)
    @JoinColumn
    private Patient patient;


    // Attribute overrides are used so that 3 doctor objects can be embedded in the task table without clashing repeated column names
    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "name", column = @Column(name = "creatorName")),
            @AttributeOverride(name = "grade", column = @Column(name = "creatorGrade"))
    })
    private Doctor creator;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "name", column = @Column(name = "completerName")),
            @AttributeOverride(name = "grade", column = @Column(name = "completerGrade"))
    })
    private Doctor completer;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "name", column = @Column(name = "plannedCompleterName")),
            @AttributeOverride(name = "grade", column = @Column(name = "plannedCompleterGrade"))
    })
    private Doctor plannedCompleter;

    public Task() {}

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public Date getDateCompleted() {
        return dateCompleted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGradeRequired() {
        return gradeRequired;
    }

    public void setGradeRequired(String gradeRequired) {
        this.gradeRequired = gradeRequired;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getCreator() {
        return creator;
    }

    public void setCreator(Doctor creator) {
        this.creator = creator;
    }

    public Doctor getCompleter() {
        return completer;
    }

    public void setCompleter(Doctor completer) {
        this.completer = completer;
    }

    public long getId() {
        return id;
    }

    public void setDateCompleted(Date dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    public Doctor getPlannedCompleter() {
        return plannedCompleter;
    }

    public void setPlannedCompleter(Doctor plannedCompleter) {
        this.plannedCompleter = plannedCompleter;
    }
}
