package ru.kopaev.todo.task;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kopaev.todo.category.exceptions.CategoryDoesNotBelongToUserException;
import ru.kopaev.todo.category.exceptions.CategoryNotFoundException;
import ru.kopaev.todo.task.dto.TaskRequest;
import ru.kopaev.todo.task.exceptions.TaskDoesNotBelongToUser;
import ru.kopaev.todo.task.exceptions.TaskNotFoundException;
import ru.kopaev.todo.user.exceptions.UserNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }
    @GetMapping("/switch")
    public ResponseEntity<String> switchDone(@RequestParam Integer id) {
        taskService.switchDone(id);
        return ResponseEntity.status(HttpStatus.OK).body("Task was updated");
    }

    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestBody TaskRequest request) {
        Task task = taskService.createTask(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @PutMapping("/edit")
    public ResponseEntity<Task> updateTask(@RequestBody TaskRequest request, @RequestParam Integer id) {
        Task updatedTask = taskService.updateTask(request, id);
        return ResponseEntity.status(HttpStatus.OK).body(updatedTask);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteTask(@RequestParam Integer id) {
        taskService.findById(id).orElseThrow(TaskNotFoundException::new);
        taskService.checkAffiliation(id);
        taskService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Task was deleted!");
    }

    @ExceptionHandler
    public ResponseEntity<String> handleCategoryNotFoundException(CategoryNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found!");
    }

    @ExceptionHandler
    public ResponseEntity<String> handleTaskNotFoundException(TaskNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found!");
    }

    @ExceptionHandler
    public ResponseEntity<String> handleTaskDoesNotBelongToUser(TaskDoesNotBelongToUser e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task doesn't belong to user!");
    }

    @ExceptionHandler ResponseEntity<String> handleCategoryDoesNotBelongToUser(CategoryDoesNotBelongToUserException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Category doesn't belong to user!");
    }

    @ExceptionHandler
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
    }
}
