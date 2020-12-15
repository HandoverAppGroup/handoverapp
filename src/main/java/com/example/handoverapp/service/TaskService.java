package com.example.handoverapp.service;

import com.example.handoverapp.dto.TaskDTO;
import com.example.handoverapp.entity.Doctor;
import com.example.handoverapp.entity.Patient;
import com.example.handoverapp.entity.Task;
import com.example.handoverapp.repository.PatientRepository;
import com.example.handoverapp.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskService {

    TaskRepository tr;
    PatientRepository pr;

    public TaskService(TaskRepository tr, PatientRepository pr) {
        this.tr = tr;
        this.pr = pr;
    }

    public List<TaskDTO> getAll(Pageable pageable) {
        Page<Task> taskPage = tr.findAllOrderByDate(pageable);
        List<Task> allTasks = taskPage.getContent();
        return allTasks.stream().map(TaskDTO::new).collect(Collectors.toList());
    }

    public List<TaskDTO> getAllBetweenDates(Date date1, Date date2, Pageable pageable) {
        Page<Task> taskPage = tr.findTasksBetween(date1, date2, pageable);
        List<Task> allTasks = taskPage.getContent();
        return allTasks.stream().map(TaskDTO::new).collect(Collectors.toList());
    }

    public List<TaskDTO> getTasksForPatient(String mrn, Pageable pageable) {
        Page<Task> taskPage = tr.findByPatientMrn(mrn, pageable);
        List<Task> allTasks = taskPage.getContent();
        return allTasks.stream().map(TaskDTO::new).collect(Collectors.toList());
    }

    public List<TaskDTO> getByCompleted(boolean completed, Pageable pageable) {
        Page<Task> taskPage = tr.findByCompleted(completed, pageable);
        List<Task> allTasks = taskPage.getContent();
        return allTasks.stream().map(TaskDTO::new).collect(Collectors.toList());
    }

    public Optional<TaskDTO> getById(long id) {
        Optional<Task> t = tr.findById(id);
        return t.map(TaskDTO::new);
    }

    public void delete(long id) {
        tr.deleteById(id);
    }

    public Optional<TaskDTO> update(TaskDTO t, long id) {
        Optional<Task> opTask = tr.findById(id);
        if (opTask.isPresent()) {
            Task updated = updateFromDTO(t, opTask.get());
            return Optional.of(new TaskDTO(updated));
        } else {
            return Optional.empty();
        }
    }

    public Optional<TaskDTO> complete(Doctor d, long id) {
        Optional<Task> opTask = tr.findById(id);
        if (opTask.isPresent()) {
            Task completed = completeTask(opTask.get(), d);
            return Optional.of(new TaskDTO(completed));
        } else {
            return Optional.empty();
        }
    }

    public Optional<TaskDTO> claim(Doctor d, long id) {
        Optional<Task> opTask = tr.findById(id);
        if (opTask.isPresent()) {
            Task claimed = claimTask(opTask.get(), d);
            return Optional.of(new TaskDTO(claimed));
        } else {
            return Optional.empty();
        }
    }

    public TaskDTO create(TaskDTO t) {
        return new TaskDTO(updateFromDTO(t, new Task()));
    }


    private static boolean isNotNullOrEmpty(String s) {
        return s != null && !s.trim().isEmpty();
    }

    private Patient updatePatientOrCreateNew(String mrn, String clinicalSummary, String location) {
        Optional<Patient> p = pr.findByMrn(mrn);
        Patient patient;
        if (p.isPresent()) {
            patient = p.get();
        } else {
            patient = new Patient();
            patient.setMrn(mrn);
        }
        if (isNotNullOrEmpty(location)) {
            patient.setLocation(location);
        }
        if (isNotNullOrEmpty(clinicalSummary)) {
            patient.setClinicalSummary(clinicalSummary);
        }
        return pr.save(patient);
    }

    private Task updateFromDTO(TaskDTO taskDTO, Task taskToUpdate) {
        String mrn = taskDTO.getPatientMrn();
        if (isNotNullOrEmpty(mrn)) {
            taskToUpdate.setPatient(updatePatientOrCreateNew(mrn, taskDTO.getPatientClinicalSummary(), taskDTO.getPatientLocation()));
        }
        String d = taskDTO.getDescription();
        if (isNotNullOrEmpty(d)) {
            taskToUpdate.setDescription(d);
        }
        String g = taskDTO.getGradeRequired();
        if (isNotNullOrEmpty(g)) {
            taskToUpdate.setGradeRequired(g);
        }
        taskToUpdate.setCompleter(taskDTO.getCompleter());
        taskToUpdate.setCreator(taskDTO.getCreator());
        taskToUpdate.setPlannedCompleter(taskDTO.getPlannedCompleter());
        boolean isCompleted = taskDTO.isCompleted();
        if (isCompleted && !taskToUpdate.isCompleted()){
            taskToUpdate.setCompleted(taskDTO.isCompleted());
            taskToUpdate.setDateCompleted(new Date());
        } else if (!isCompleted) {
            taskToUpdate.setCompleted(false);
            taskToUpdate.setDateCompleted(null);
            taskToUpdate.setCompleter(null);
        }
        return tr.save(taskToUpdate);
    }

    private Task completeTask(Task task, Doctor doctor) {
        task.setCompleted(true);
        task.setDateCompleted(new Date());
        task.setCompleter(doctor);
        return tr.save(task);
    }

    private Task claimTask(Task task, Doctor doctor) {
        task.setPlannedCompleter(doctor);
        return tr.save(task);
    }



}