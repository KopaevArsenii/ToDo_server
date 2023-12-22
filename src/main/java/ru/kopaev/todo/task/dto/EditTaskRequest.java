package ru.kopaev.todo.task.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EditTaskRequest {
    private String name;
    private String description;
    private Integer categoryId;

}
