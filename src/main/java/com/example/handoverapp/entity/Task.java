package com.example.handoverapp.entity;

import com.example.handoverapp.dto.TaskDTO;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;


@Entity
public class Task {

    @Id
    @GeneratedValue
    private long id;

    private boolean completed = false;
    private final Date dateCreated = new Date();
    private Date dateCompleted;
    private String description;
    private String gradeRequired;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn
    private Patient patient;

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
}
