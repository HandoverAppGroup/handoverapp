package com.example.handoverapp;

import com.example.handoverapp.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTests {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    public void deleteAllBeforeTests() throws Exception {
        taskRepository.deleteAll();
    }

    @Test
    public void shouldCreateEntity() throws Exception {

        mockMvc.perform(post("/api/tasks")
                .content("{\"description\":\"Do some things\",\"patientMrn\":\"12345\",\"patientLocation\":\"Ward B bed 2\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("tasks/")));
    }

    @Test
    public void shouldGetAllTasks() throws Exception {

        mockMvc.perform(post("/api/tasks")
                .content("{\"description\":\"Do some things\",\"patientMrn\":\"12345\",\"patientLocation\":\"Ward B bed 2\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("tasks/")));

        mockMvc.perform(post("/api/tasks")
                .content("{\"description\":\"Do some other things\",\"patientMrn\":\"12345\",\"patientLocation\":\"Ward B bed 2\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("tasks/")));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)));
    }

    @Test
    public void completeTaskShouldSetCompletionDateAndDoctor() throws Exception {
        // GIVEN

        MvcResult mvcResult = mockMvc.perform(post("/api/tasks")
                .content("{\"description\":\"Do some things\",\"patientMrn\":\"12345\",\"patientLocation\":\"Ward B bed 2\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String location = mvcResult.getResponse().getHeader("Location");

        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completed").value(false))
                .andExpect(jsonPath("$.dateCompleted").isEmpty());

        // WHEN

        mockMvc.perform(post(location+"/complete")
                .content("{\"name\":\"Dr Stephens\",\"grade\":\"A\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // THEN

        mockMvc.perform(get(location)).andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Do some things"))
                .andExpect(jsonPath("$.completer.name").value("Dr Stephens"))
                .andExpect(jsonPath("$.completer.grade").value("A"))
                .andExpect(jsonPath("$.completed").value(true))
                .andExpect(jsonPath("$.dateCompleted").isNotEmpty());

    }

    @Test
    public void claimTaskShouldSetPlannedCompleter() throws Exception {
        // GIVEN

        MvcResult mvcResult = mockMvc.perform(post("/api/tasks")
                .content("{\"description\":\"Do some things\",\"patientMrn\":\"12345\",\"patientLocation\":\"Ward B bed 2\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String location = mvcResult.getResponse().getHeader("Location");

        // WHEN

        mockMvc.perform(post(location+"/claim")
                .content("{\"name\":\"Dr Stephens\",\"grade\":\"A\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // THEN

        mockMvc.perform(get(location)).andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Do some things"))
                .andExpect(jsonPath("$.plannedCompleter.name").value("Dr Stephens"))
                .andExpect(jsonPath("$.plannedCompleter.grade").value("A"));

    }


    @Test
    public void shouldRetrieveEntity() throws Exception {

        MvcResult mvcResult = mockMvc.perform(post("/api/tasks")
                .content("{\"description\":\"Do some things\",\"patientMrn\":\"12345\",\"patientLocation\":\"Ward B bed 2\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String location = mvcResult.getResponse().getHeader("Location");

        mockMvc.perform(get(location)).andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Do some things"))
                .andExpect(jsonPath("$.patientMrn").value("12345"))
                .andExpect(jsonPath("$.patientLocation").value("Ward B bed 2"));
    }


    @Test
    public void shouldRetrieveUncompletedTasks() throws Exception {

        mockMvc.perform(post("/api/tasks")
                .content("{\"description\":\"Do some things\",\"patientMrn\":\"12345\",\"patientLocation\":\"Ward B bed 2\",\"completed\":false}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/tasks")
                .content("{\"description\":\"Do some other things\",\"patientMrn\":\"12345\",\"patientLocation\":\"Ward B bed 2\",\"completed\":true}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(
                get("/api/tasks/uncompleted"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].description").value("Do some things"))
                .andExpect(jsonPath("$",hasSize(1)));
    }

    @Test
    public void shouldUpdateEntity() throws Exception {

        MvcResult mvcResult = mockMvc.perform(post("/api/tasks")
                .content("{\"description\":\"Do some things\",\"patientMrn\":\"12345\",\"patientLocation\":\"Ward B bed 2\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String location = mvcResult.getResponse().getHeader("Location");

        mockMvc.perform(put(location)
                .content("{\"description\":\"Do some things\",\"patientMrn\":\"12345\",\"patientLocation\":\"Ward A bed 2\",\"plannedCompleter\":{\"name\":\"Jennifer\", \"grade\":\"A\"}}}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Do some things"))
                .andExpect(jsonPath("$.plannedCompleter.name").value("Jennifer"))
                .andExpect(jsonPath("$.plannedCompleter.grade").value("A"))
                .andExpect(jsonPath("$.patientLocation").value("Ward A bed 2"));
    }

    @Test
    public void shouldDeleteEntity() throws Exception {

        MvcResult mvcResult = mockMvc.perform(post("/api/tasks")
                .content("{\"description\":\"Do some things\",\"patientMrn\":\"12345\",\"patientLocation\":\"Ward A bed 2\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String location = mvcResult.getResponse().getHeader("Location");
        mockMvc.perform(delete(location)).andExpect(status().isNoContent());

        mockMvc.perform(get(location)).andExpect(status().isNotFound());
    }


}
