package ru.kopaev.todo.task;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kopaev.todo.category.Category;
import ru.kopaev.todo.category.CategoryService;
import ru.kopaev.todo.config.JwtService;
import ru.kopaev.todo.task.dto.CreateTaskRequest;
import ru.kopaev.todo.user.User;
import ru.kopaev.todo.user.UserService;

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

        User user = userService.findByEmail(userEmail).get();

        Category category = categoryService.findById(request.getCategoryId()).get();

        Task newTask = Task.builder()
                .name(request.getName())
                .description(request.getDescription())
                .category(category)
                .user(user)
                .build();
        taskService.createTask(newTask);
        return ResponseEntity.ok().body("Task was created!");
    }
}
