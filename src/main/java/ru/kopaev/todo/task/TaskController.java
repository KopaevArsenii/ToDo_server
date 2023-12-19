package ru.kopaev.todo.task;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kopaev.todo.task.dto.CreateTaskRequest;

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    @PostMapping("/create")
    public ResponseEntity<String> createTask(@RequestBody CreateTaskRequest request, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {
        taskService.createNewTask(request, header.substring(7));
        return ResponseEntity.ok().body("Task was created!");
    }
}
