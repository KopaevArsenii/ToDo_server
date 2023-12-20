package ru.kopaev.todo.task;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kopaev.todo.category.Category;
import ru.kopaev.todo.category.CategoryService;
import ru.kopaev.todo.category.exceptions.CategoryNotFoundException;
import ru.kopaev.todo.config.JwtService;
import ru.kopaev.todo.task.dto.CreateTaskRequest;
import ru.kopaev.todo.user.User;
import ru.kopaev.todo.user.UserService;
import ru.kopaev.todo.user.exceptions.UserNotFoundException;

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final JwtService jwtService;
    private final UserService userService;
    private final CategoryService categoryService;
    @PostMapping("/create")
    public ResponseEntity<String> createTask(@RequestBody CreateTaskRequest request, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {
        String token = jwtService.extractTokenFromHeader(header);
        String userEmail = jwtService.extractUsername(token);

        User user = userService.findByEmail(userEmail).orElseThrow(UserNotFoundException::new);
        Category category = categoryService.findById(request.getCategoryId()).orElseThrow(CategoryNotFoundException::new);

        Task newTask = Task.builder()
                .name(request.getName())
                .description(request.getDescription())
                .category(category)
                .user(user)
                .build();
        taskService.createTask(newTask);
        return ResponseEntity.ok().body("Task was created!");
    }

    @ExceptionHandler
    public ResponseEntity<String> handleCategoryNotFoundException(CategoryNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found!");
    }

    @ExceptionHandler
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
    }
}
