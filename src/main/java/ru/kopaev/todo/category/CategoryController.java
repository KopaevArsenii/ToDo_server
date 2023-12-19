package ru.kopaev.todo.category;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.kopaev.todo.category.dto.CreateCategoryRequest;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    @PostMapping("/create")
    public ResponseEntity<String> createCategory(@RequestBody CreateCategoryRequest request, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {
        categoryService.createNewCategory(request, header.substring(7));
        return ResponseEntity.status(HttpStatus.CREATED).body("New category was created!");
    }
}
