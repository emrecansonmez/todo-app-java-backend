package com.thy.todoapp.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TaskCreateDto {
    private String title;
    private String description;
    private Integer priority;
    private Integer state;

}
