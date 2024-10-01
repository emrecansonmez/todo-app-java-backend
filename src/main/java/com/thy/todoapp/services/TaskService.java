package com.thy.todoapp.services;

import com.thy.todoapp.dtos.TaskCreateDto;
import com.thy.todoapp.entities.Task;
import com.thy.todoapp.entities.User;
import com.thy.todoapp.repositories.TaskRepository;
import com.thy.todoapp.repositories.UserRepository;
import com.thy.todoapp.specifications.TaskSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final UserService userService;


    public Task createTask(TaskCreateDto taskCreateDTO) {
        String currentUserEmail = userService.getCurrentUsername();
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
        String currentUserEmail = userService.getCurrentUsername();

        Specification<Task> spec = Specification.where(TaskSpecification.belongsToUser(currentUserEmail))
                .and(TaskSpecification.hasName(name))
                .and(TaskSpecification.hasPriority(priority))
                .and(TaskSpecification.hasState(state))
                .and(TaskSpecification.hasCaseInsensitiveSort(sortBy, direction));

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        return taskRepository.findAll(spec, sortedPageable);
    }

    public Task updateTask(Long id, Task taskDetails) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        String currentUsername = userService.getCurrentUsername();
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

        String currentUsername = userService.getCurrentUsername();
        if (!task.getUser().getUsername().equals(currentUsername)) {
            throw new RuntimeException("You can only delete your own tasks!");
        }

        taskRepository.deleteById(id);
    }
}
