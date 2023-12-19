package ru.kopaev.todo.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class CreateCategoryRequest {
    private String name;
    private String description;
}
