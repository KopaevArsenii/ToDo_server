package ru.kopaev.todo.category.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EditCategoryRequest {
    private String name;
    private String description;
    private Integer id;
}
