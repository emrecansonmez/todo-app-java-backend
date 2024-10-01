package com.example.todoapp.services;

import com.example.todoapp.dtos.TaskCreateDto;
import com.example.todoapp.entities.Task;
import com.example.todoapp.entities.User;
import com.example.todoapp.repositories.TaskRepository;
import com.example.todoapp.repositories.UserRepository;
import com.example.todoapp.specifications.TaskSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private  UserRepository userRepository;


    public Task createTask(TaskCreateDto taskCreateDTO) {
        String currentUserEmail = getCurrentUsername();
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Task task = new Task();
        task.setTitle(taskCreateDTO.getTitle());
        task.setDescription(taskCreateDTO.getDescription());
        task.setPriority(taskCreateDTO.getPriority());
        task.setState(taskCreateDTO.getState());
        task.setUser(user);

        return taskRepository.save(task);
    }


    public Page<Task> getTasksForCurrentUser(String name, String priority, String state, String sortBy, String direction, Pageable pageable) {
        String currentUserEmail = getCurrentUsername();

        Specification<Task> spec = Specification.where(TaskSpecification.belongsToUser(currentUserEmail))
                .and(TaskSpecification.hasName(name))
                .and(TaskSpecification.hasPriority(priority))
                .and(TaskSpecification.hasState(state))
                .and(TaskSpecification.hasCaseInsensitiveSort(sortBy, direction));

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        return taskRepository.findAll(spec, sortedPageable);
    }


    public String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }


    public Task updateTask(Long id, Task taskDetails) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        String currentUsername = getCurrentUsername();
        if (!task.getUser().getUsername().equals(currentUsername)) {
            throw new RuntimeException("You can only update your own tasks!");
        }


        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setPriority(taskDetails.getPriority());
        task.setState(taskDetails.getState());

        return taskRepository.save(task);
    }
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        String currentUsername = getCurrentUsername();
        if (!task.getUser().getUsername().equals(currentUsername)) {
            throw new RuntimeException("You can only delete your own tasks!");
        }

        taskRepository.deleteById(id);
    }
}
