package com.thy.todoapp.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    @Column(nullable = false)
    private String title;

    private String description;


    @Column(nullable = false)
    private Integer priority;


    @Column(nullable = false)
    private Integer state;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


}
