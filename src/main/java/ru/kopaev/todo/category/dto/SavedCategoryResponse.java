package ru.kopaev.todo.category.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SavedCategoryResponse {
    private Integer id;
    private String name;
    private String description;
}
