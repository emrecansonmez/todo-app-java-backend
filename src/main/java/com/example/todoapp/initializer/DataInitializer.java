package com.example.todoapp.initializer;

import com.example.todoapp.entities.Task;
import com.example.todoapp.entities.User;
import com.example.todoapp.repositories.TaskRepository;
import com.example.todoapp.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, TaskRepository taskRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Kullanıcı ekleme
            if (userRepository.count() == 0) {
                User user1 = new User();
                user1.setFullName("John Doe");
                user1.setEmail("admin@gmail.com");
                user1.setPassword(passwordEncoder.encode("123123"));
                userRepository.save(user1);

                User user2 = new User();
                user2.setFullName("Jane Doe");
                user2.setEmail("jane.doe@example.com");
                user2.setPassword(passwordEncoder.encode("123"));
                userRepository.save(user2);


                for (int i = 1; i <= 30; i++) {
                    Task task = new Task();
                    task.setTitle("Task " + i + " - John");
                    task.setDescription("John's task number " + i);
                    task.setPriority((i % 4) + 1);
                    task.setState((i % 2) + 1);
                    task.setUser(user1);
                    taskRepository.save(task);
                }


                Task task3 = new Task();
                task3.setTitle("Task 1 - Jane");
                task3.setDescription("Jane's first task");
                task3.setPriority(1);
                task3.setState(2);
                task3.setUser(user2);
                taskRepository.save(task3);
            }

            System.out.println("Database has been initialized with sample data.");
        };
    }
}
