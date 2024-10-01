package com.thy.todoapp.services;

import com.thy.todoapp.dtos.TaskCreateDto;
import com.thy.todoapp.entities.Task;
import com.thy.todoapp.entities.User;
import com.thy.todoapp.repositories.TaskRepository;
import com.thy.todoapp.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTask_ShouldCreateTaskSuccessfully() {
        TaskCreateDto taskCreateDto = new TaskCreateDto();
        taskCreateDto.setTitle("Test Task");
        taskCreateDto.setDescription("Test Description");
        taskCreateDto.setPriority(1);
        taskCreateDto.setState(1);

        User mockUser = new User();
        mockUser.setEmail("test@test.com");

        when(userService.getCurrentUsername()).thenReturn("test@test.com");
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(mockUser));

        Task savedTask = new Task();
        savedTask.setTitle("Test Task");
        savedTask.setDescription("Test Description");
        savedTask.setPriority(1);
        savedTask.setState(1);

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        Task result = taskService.createTask(taskCreateDto);

        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void getTasksForCurrentUser_ShouldReturnTasks() {
        Pageable pageable = PageRequest.of(0, 1);
        Task mockTask = new Task();
        mockTask.setTitle("Test Task");

        Page<Task> mockPage = new PageImpl<>(Collections.singletonList(mockTask));

        when(userService.getCurrentUsername()).thenReturn("test@test.com");
        when(taskRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(mockPage);
        Page<Task> result = taskService.getTasksForCurrentUser(null, null, null, null, null, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Task", result.getContent().get(0).getTitle());
    }

    @Test
    void updateTask_ShouldUpdateTaskSuccessfully() {
        Task existingTask = new Task();
        existingTask.setTaskId(1L);
        existingTask.setTitle("Old Task");
        User user = new User();
        user.setEmail("testuser@example.com");
        existingTask.setUser(user);

        Task updatedTaskDetails = new Task();
        updatedTaskDetails.setTitle("Updated Task");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(userService.getCurrentUsername()).thenReturn("testuser@example.com");
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

        Task result = taskService.updateTask(1L, updatedTaskDetails);

        assertNotNull(result);
        assertEquals("Updated Task", result.getTitle());
        verify(taskRepository).save(existingTask);
    }

    @Test
    void deleteTask_ShouldDeleteTaskSuccessfully() {
        Task existingTask = new Task();
        existingTask.setTaskId(1L);
        User user = new User();
        user.setEmail("testuser@example.com");
        existingTask.setUser(user);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(userService.getCurrentUsername()).thenReturn("testuser@example.com");

        taskService.deleteTask(1L);

        verify(taskRepository).deleteById(1L);
    }
}
