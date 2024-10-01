package com.example.todoapp.controllers;

import com.example.todoapp.dtos.TaskCreateDto;
import com.example.todoapp.entities.Task;
import com.example.todoapp.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestBody TaskCreateDto taskCreateDTO) {
        Task createdTask = taskService.createTask(taskCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }
    @GetMapping("/allTasks")
    public Page<Task> getTasks(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction,
            Pageable pageable) {
        return taskService.getTasksForCurrentUser(name, priority, state, sortBy, direction, pageable);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task taskDetails) {
        Task updatedTask = taskService.updateTask(id, taskDetails);
        return ResponseEntity.ok(updatedTask);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
