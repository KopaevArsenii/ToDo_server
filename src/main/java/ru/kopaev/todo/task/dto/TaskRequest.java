package ru.kopaev.todo.task.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskRequest {
    private String name;
    private String description;
    private Integer categoryId;
}
