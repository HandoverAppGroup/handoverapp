package com.example.handoverapp.dto;

import com.example.handoverapp.entity.Doctor;
import com.example.handoverapp.entity.Patient;
import com.example.handoverapp.entity.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskDTOTest {

    @Test
    void dtoFromEntity() {
        Task entity = new Task();
        entity.setGradeRequired("ABC");
        entity.setDescription("Do these things");
        entity.setPatient(new Patient("ABC123","Very ill","Ward 2 Bed 5"));
        entity.setCreator(new Doctor("Dr Stephens","B"));
        entity.setPlannedCompleter(new Doctor("Dr Davidson","C"));
        TaskDTO dto = new TaskDTO(entity);
        assertEquals(dto.getPatientMrn(), "ABC123");
        assertEquals(dto.getPatientLocation(), "Ward 2 Bed 5");
        assertEquals(dto.getPatientClinicalSummary(), "Very ill");
        assertEquals(dto.getCreator().getName(), "Dr Stephens");
        assertEquals(dto.getCreator().getGrade(), "B");
        assertEquals(dto.getPlannedCompleter().getGrade(), "C");
        assertEquals(dto.getGradeRequired(), "ABC");
        assertEquals(dto.getDescription(), "Do these things");
    }
}