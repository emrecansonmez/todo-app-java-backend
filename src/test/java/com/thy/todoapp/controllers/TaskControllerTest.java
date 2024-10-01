package com.thy.todoapp.controllers;

import com.thy.todoapp.dtos.TaskCreateDto;
import com.thy.todoapp.entities.Task;
import com.thy.todoapp.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Collections;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TaskControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(taskController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void createTask_ShouldReturnCreatedTask() throws Exception {
        TaskCreateDto taskCreateDto = new TaskCreateDto();
        taskCreateDto.setTitle("Test Task");
        taskCreateDto.setDescription("Test Description");
        taskCreateDto.setPriority(1);
        taskCreateDto.setState(1);

        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Test Description");

        when(taskService.createTask(any(TaskCreateDto.class))).thenReturn(task);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Test Task\", \"description\":\"Test Description\", \"priority\":1, \"state\":1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.description").value("Test Description"));

        verify(taskService).createTask(any(TaskCreateDto.class));
    }

    @Test
    void getTasks_ShouldReturnTaskPage() throws Exception {
        Task task = new Task();
        task.setTitle("Test Task");

        Page<Task> page = new PageImpl<>(Collections.singletonList(task), PageRequest.of(0, 10), 1);

        when(taskService.getTasksForCurrentUser(any(), any(), any(), any(), any(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/tasks")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(taskService).getTasksForCurrentUser(any(), any(), any(), any(), any(), any(Pageable.class));
    }

    @Test
    void updateTask_ShouldReturnUpdatedTask() throws Exception {
        Task task = new Task();
        task.setTitle("Updated Task");

        when(taskService.updateTask(anyLong(), any(Task.class))).thenReturn(task);

        mockMvc.perform(put("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated Task\", \"description\":\"Updated Description\", \"priority\":1, \"state\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"));

        verify(taskService).updateTask(anyLong(), any(Task.class));
    }

    @Test
    void deleteTask_ShouldReturnNoContent() throws Exception {
        doNothing().when(taskService).deleteTask(anyLong());

        mockMvc.perform(delete("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(taskService).deleteTask(anyLong());
    }
}
