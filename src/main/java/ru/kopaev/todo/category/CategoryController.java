package ru.kopaev.todo.category;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kopaev.todo.category.dto.CreateCategoryRequest;
import ru.kopaev.todo.config.JwtService;
import ru.kopaev.todo.user.User;
import ru.kopaev.todo.user.UserService;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final JwtService jwtService;
    private final UserService userService;
    @PostMapping("/create")
    public ResponseEntity<String> createCategory(@RequestBody CreateCategoryRequest request, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {
        String token = jwtService.extractTokenFromHeader(header);
        String userEmail = jwtService.extractUsername(token);

        User user = userService.findByEmail(userEmail).get();

        Category newCategory = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .user(user)
                .build();

        categoryService.createNewCategory(newCategory);

        return ResponseEntity.status(HttpStatus.CREATED).body("New category was created!");
    }
}
