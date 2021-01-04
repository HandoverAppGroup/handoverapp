package com.example.handoverapp.dto;

import com.example.handoverapp.entity.Doctor;
import com.example.handoverapp.entity.Patient;
import com.example.handoverapp.entity.Task;

import javax.validation.constraints.NotBlank;
import java.util.Date;

// Create patients and tasks using a single DTO
public class TaskDTO {

    private long id;
    private boolean completed;
    private Date dateCreated;
    private Date dateCompleted;
    @NotBlank(message = "Please provide task description")
    private String description;
    private String gradeRequired;

    // Used to generate a new Patient object if none exists for this mrn
    @NotBlank(message = "Please provide patient mrn")
    private String patientMrn;
    private String patientClinicalSummary;
    @NotBlank(message = "Please provide patient location")
    private String patientLocation;

    // Doctors are embedded in the Task table
    private Doctor creator;
    private Doctor completer;
    private Doctor plannedCompleter;

    public TaskDTO() {}

    public TaskDTO(Task t) {
        Patient p = t.getPatient();
        this.completed = t.isCompleted();
        this.dateCreated = t.getDateCreated();
        this.dateCompleted = t.getDateCompleted();
        this.description = t.getDescription();
        this.gradeRequired = t.getGradeRequired();
        this.completer = t.getCompleter();
        this.creator = t.getCreator();
        this.plannedCompleter = t.getPlannedCompleter();
        this.id = t.getId();
        if (p != null) {
            this.patientMrn = p.getMrn();
            this.patientClinicalSummary = p.getClinicalSummary();
            this.patientLocation = p.getLocation();
        }
    }

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

    public String getPatientMrn() {
        return patientMrn;
    }

    public void setPatientMrn(String patientMrn) {
        this.patientMrn = patientMrn;
    }

    public String getPatientClinicalSummary() {
        return patientClinicalSummary;
    }

    public void setPatientClinicalSummary(String patientClinicalSummary) {
        this.patientClinicalSummary = patientClinicalSummary;
    }

    public String getPatientLocation() {
        return patientLocation;
    }

    public void setPatientLocation(String patientLocation) {
        this.patientLocation = patientLocation;
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

    public Doctor getPlannedCompleter() {
        return plannedCompleter;
    }

    public void setPlannedCompleter(Doctor plannedCompleter) {
        this.plannedCompleter = plannedCompleter;
    }
}
