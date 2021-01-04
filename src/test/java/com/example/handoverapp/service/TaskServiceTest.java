package com.example.handoverapp.service;

import com.example.handoverapp.controller.DateUtils;
import com.example.handoverapp.dto.TaskDTO;
import com.example.handoverapp.entity.Doctor;
import com.example.handoverapp.entity.Patient;
import com.example.handoverapp.entity.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TaskServiceTest {

    @Autowired
    TaskService service;

    PageRequest pageRequest = PageRequest.of(0,30);

    @BeforeEach
    public void deleteAllBeforeTests() throws Exception {
        service.tr.deleteAll();
        service.pr.deleteAll();
        Task task = new Task();
        Doctor cr = new Doctor("Dr Stephens", "A");
        Patient p = new Patient("123", "Very ill", "Ward 3 Bed 1");
        p = service.pr.save(p);
        task.setPatient(p);
        task.setCreator(cr);
        task.setDescription("Do some stuff");
        service.tr.save(task);

    }

    @Test
    void getAll() {
        List<TaskDTO> all = service.getAll(pageRequest);
        assertEquals(all.size(), 1);
        assertEquals(all.get(0).getDescription(), "Do some stuff");
        assertEquals(all.get(0).getPatientMrn(), "123");
    }

    @Test
    void getAllBetweenDates() {
        List<TaskDTO> all = service.getAllBetweenDates(DateUtils.getRecent(), new Date(), pageRequest);
        assertEquals(all.size(), 1);
        assertEquals(all.get(0).getDescription(), "Do some stuff");
        assertEquals(all.get(0).getPatientMrn(), "123");
    }

    @Test
    void getTasksForPatient() {
        // GIVEN
        Task task = new Task();
        Patient p = new Patient("12345", "Very ill", "Ward 3 Bed 1");
        p = service.pr.save(p);
        task.setPatient(p);
        task.setDescription("Do some more stuff");
        service.tr.save(task);
        // WHEN
        List<TaskDTO> tasks = service.getTasksForPatient("12345",pageRequest);
        // THEN
        assertEquals(tasks.size(), 1);
        assertEquals(tasks.get(0).getDescription(), "Do some more stuff");
        assertEquals(tasks.get(0).getPatientMrn(), "12345");
    }

    @Test
    void getByCompleted() {
        // GIVEN
        Task task = new Task();
        task.setCompleted(true);
        service.tr.save(task);
        // WHEN
        List<TaskDTO> tasks = service.getByCompleted(false, pageRequest);
        // THEN
        assertEquals(tasks.size(), 1);
        assertEquals(tasks.get(0).getDescription(), "Do some stuff");
        assertEquals(tasks.get(0).getPatientMrn(), "123");
    }

    @Test
    void getById() {
        // GIVEN
        Task task = new Task();
        task.setDescription("Banana");
        task = service.tr.save(task);
        // WHEN
        Optional<TaskDTO> taskDto = service.getById(task.getId());
        // THEN
        assertTrue(taskDto.isPresent());
        assertEquals(taskDto.get().getDescription(), "Banana");

    }

    @Test
    void getByIdDoesNotExist() {
        // GIVEN
        Task task = new Task();
        task.setDescription("Banana");
        task = service.tr.save(task);
        // WHEN
        Optional<TaskDTO> taskDto = service.getById(10101);
        // THEN
        assertTrue(taskDto.isEmpty());
    }

    @Test
    void delete() {
        // GIVEN
        Task task = new Task();
        task.setDescription("Banana");
        task = service.tr.save(task);
        // WHEN
        service.delete(task.getId());
        // THEN
        assertEquals(service.tr.findAll().size(), 1);
    }

    @Test
    void updateCanCreateNewPatient() {
        // GIVEN
        Task task = new Task();
        task.setDescription("Banana");
        task = service.tr.save(task);
        // WHEN
        TaskDTO dto = new TaskDTO();
        dto.setDescription("A random task");
        dto.setPatientMrn("12345678");
        dto.setCreator(new Doctor("Dr Woods", "B"));
        Optional<TaskDTO> updated = service.update(dto, task.getId());
        // THEN
        assertTrue(updated.isPresent());
        assertTrue(service.pr.findByMrn("12345678").isPresent());
        assertEquals(updated.get().getPatientMrn(), "12345678");
        assertEquals(updated.get().getCreator().getName(), "Dr Woods");
        assertEquals(updated.get().getDescription(), "A random task");
    }

    @Test
    void updateCanUseExistingPatient() {
        // GIVEN
        Task task = new Task();
        task.setDescription("Banana");
        task = service.tr.save(task);
        Patient p = new Patient();
        p.setMrn("99");
        p.setClinicalSummary("Blah blah blah");
        service.pr.save(p);
        // WHEN
        TaskDTO dto = new TaskDTO();
        dto.setPatientMrn("99");
        Optional<TaskDTO> updated = service.update(dto, task.getId());
        // THEN
        assertTrue(updated.isPresent());
        assertEquals(updated.get().getPatientMrn(), "99");
        assertEquals(updated.get().getPatientClinicalSummary(), "Blah blah blah");
    }

    @Test
    void updateTaskCanUpdateExistingPatient() {
        // GIVEN
        Task task = new Task();
        task.setDescription("A task description");
        task = service.tr.save(task);
        Patient p = new Patient();
        p.setMrn("99");
        p.setClinicalSummary("Blah blah blah");
        service.pr.save(p);
        // WHEN
        TaskDTO dto = new TaskDTO();
        dto.setPatientMrn("99");
        dto.setPatientClinicalSummary("A new, updated clinical summary");
        Optional<TaskDTO> updated = service.update(dto, task.getId());
        // THEN
        assertTrue(updated.isPresent());
        assertEquals(updated.get().getPatientMrn(), "99");
        assertEquals(updated.get().getPatientClinicalSummary(), "A new, updated clinical summary");
    }

    @Test
    void complete() {
        // GIVEN
        Task t = new Task();
        t = service.tr.save(t);
        Doctor d = new Doctor("Dr Stephens", "22");
        // WHEN
        Optional<TaskDTO> completed = service.complete(d, t.getId());
        // THEN
        assertTrue(completed.isPresent());
        assertEquals(completed.get().getCompleter().getName(), "Dr Stephens");
        assertEquals(completed.get().getCompleter().getGrade(), "22");
        assertTrue(completed.get().isCompleted());
        assertNotNull(completed.get().getDateCompleted());

    }

    @Test
    void claim() {
        // GIVEN
        Task t = new Task();
        t = service.tr.save(t);
        Doctor d = new Doctor("Dr Stephens", "22");
        // WHEN
        Optional<TaskDTO> claimed = service.claim(d, t.getId());
        // THEN
        assertTrue(claimed.isPresent());
        assertEquals(claimed.get().getPlannedCompleter().getName(), "Dr Stephens");
        assertEquals(claimed.get().getPlannedCompleter().getGrade(), "22");
        assertFalse(claimed.get().isCompleted());
        assertNull(claimed.get().getDateCompleted());
    }

    @Test
    void createNewTaskCanUpdateExistingPatient() {
        // GIVEN
        Patient p = new Patient();
        p.setMrn("99");
        p.setClinicalSummary("Blah blah blah");
        service.pr.save(p);
        TaskDTO dto = new TaskDTO();
        dto.setPatientMrn("99");
        dto.setPatientClinicalSummary("An updated clinical summary");
        dto.setDescription("Do some stuff");
        // WHEN
        TaskDTO created = service.create(dto);
        // THEN
        assertTrue(service.pr.findByMrn("99").isPresent());
        assertEquals(service.pr.findByMrn("99").get().getClinicalSummary(), "An updated clinical summary");
        assertEquals(created.getDescription(), "Do some stuff");
    }

    @Test
    void createNewTaskCanCreateNewPatient() {
        // GIVEN
        TaskDTO dto = new TaskDTO();
        dto.setPatientMrn("99");
        dto.setPatientClinicalSummary("A clinical summary");
        dto.setDescription("Do some stuff");
        // WHEN
        TaskDTO created = service.create(dto);
        // THEN
        assertTrue(service.pr.findByMrn("99").isPresent());
        assertEquals(service.pr.findByMrn("99").get().getClinicalSummary(),"A clinical summary");
        assertEquals(created.getDescription(), "Do some stuff");
    }
}