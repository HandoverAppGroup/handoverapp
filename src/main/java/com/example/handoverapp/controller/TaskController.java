package com.example.handoverapp.controller;

import com.example.handoverapp.dto.TaskDTO;
import com.example.handoverapp.entity.Doctor;
import com.example.handoverapp.exception.TaskNotFoundException;
import com.example.handoverapp.service.TaskService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequestMapping("/api")
@RestController
@CrossOrigin
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // GET endpoints
    // All GET endpoints are paginated by default, with 30 items per page
    // Tasks are ordered by date, with most recent tasks at the top

    // Returns all tasks
    @GetMapping("/tasks")
    ResponseEntity<List<TaskDTO>> all(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size
    ) {
        Pageable paging = PageRequest.of(page, size);
        List<TaskDTO> responseBody = taskService.getAll(paging);
        return ResponseEntity.ok().body(responseBody);
    }

    // Returns tasks in a certain date range
    @GetMapping("/tasks/byDate")
    ResponseEntity<List<TaskDTO>> byDate(
            @RequestParam String earliestDate,
            @RequestParam String latestDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size
    ) {
        Optional<Date> date1 = DateUtils.dateFromString(earliestDate);
        Optional<Date> date2 = DateUtils.dateFromString(latestDate);
        if (date1.isPresent() && date2.isPresent()) {
            Pageable paging = PageRequest.of(page, size);
            List<TaskDTO> responseBody = taskService.getAllBetweenDates(date1.get(), date2.get(), paging);
            return ResponseEntity.ok().body(responseBody);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    // Returns tasks for a patient
    @GetMapping("/tasks/byPatient")
    ResponseEntity<List<TaskDTO>> byPatient(
            @RequestParam String mrn,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size
    ) {
        Pageable paging = PageRequest.of(page, size);
        List<TaskDTO> responseBody = taskService.getTasksForPatient(mrn, paging);
        return ResponseEntity.ok().body(responseBody);
    }

    // Returns all uncompleted tasks
    @GetMapping("/tasks/uncompleted")
    ResponseEntity<List<TaskDTO>> uncompleted(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size
    ) {
        Pageable paging = PageRequest.of(page, size);
        List<TaskDTO> responseBody = taskService.getByCompleted(false, paging);
        return ResponseEntity.ok().body(responseBody);
    }


    // Returns tasks created in the last 2 days
    @GetMapping("/tasks/recent")
    ResponseEntity<List<TaskDTO>> recent(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size
    ) {
        Pageable paging = PageRequest.of(page, size);
        List<TaskDTO> responseBody = taskService.getAllBetweenDates(DateUtils.getRecent(), new Date(), paging);
        return ResponseEntity.ok().body(responseBody);
    }

    // Returns task with a certain id
    @GetMapping("/tasks/{id}")
    ResponseEntity<TaskDTO> getById(@PathVariable("id") Long id) {
        TaskDTO task = taskService.getById(id).orElseThrow(() -> new TaskNotFoundException("No task found for this id"));
        return ResponseEntity.ok().body(task);
    }

    // POST endpoints

    // Create a new task using a task dto object
    @PostMapping("/tasks")
    ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskDTO tdto) {
        TaskDTO created = taskService.create(tdto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    // Complete a task by posting doctor information to the complete endpoint
    @PostMapping("/tasks/{id}/complete")
    ResponseEntity<TaskDTO> completeTask(@PathVariable("id") Long id, @Valid @RequestBody Doctor doctor) {
        TaskDTO completed = taskService.complete(doctor, id).orElseThrow(() -> new TaskNotFoundException("No task found for this id"));
        return ResponseEntity.ok().body(completed);
    }

    // Claim a task by posting doctor information to the claim endpoint
    @PostMapping("/tasks/{id}/claim")
    ResponseEntity<TaskDTO> claimTask(@PathVariable("id") Long id, @Valid @RequestBody Doctor doctor) {
        TaskDTO claimed = taskService.claim(doctor, id).orElseThrow(() -> new TaskNotFoundException("No task found for this id"));
        return ResponseEntity.ok().body(claimed);
    }

    // PUT endpoints

    // Edit a task with an updated TaskDTO object
    // Changes in id are ignored as this is a put endpoint
    @PutMapping("/tasks/{id}")
    ResponseEntity<TaskDTO> update(@PathVariable("id") Long id, @Valid @RequestBody TaskDTO tdto) {
        TaskDTO task = taskService.update(tdto, id).orElseThrow(() -> new TaskNotFoundException("No task found for this id"));
        return ResponseEntity.ok().body(task);
    }

    // DELETE endpoints

    // Delete a task with a certain id
    @DeleteMapping("/tasks/{id}")
    ResponseEntity<?> deleteTask(@PathVariable("id") Long id) {
        TaskDTO task = taskService.getById(id).orElseThrow(() -> new TaskNotFoundException("No task found for this id"));
        taskService.delete(task.getId());
        return ResponseEntity.status(204).build();
    }
}
