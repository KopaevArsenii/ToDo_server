package ru.kopaev.todo.task;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.kopaev.todo.category.Category;
import ru.kopaev.todo.category.CategoryService;
import ru.kopaev.todo.category.exceptions.CategoryNotFoundException;
import ru.kopaev.todo.task.dto.TaskRequest;
import ru.kopaev.todo.task.exceptions.TaskDoesNotBelongToUser;
import ru.kopaev.todo.task.exceptions.TaskNotFoundException;
import ru.kopaev.todo.user.User;
import ru.kopaev.todo.user.UserService;
import ru.kopaev.todo.user.exceptions.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final CategoryService categoryService;
    private final UserService userService;

    public List<Task> getAllTasks() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Task> tasks = userService
                .findByEmail(authentication.getName())
                .orElseThrow(UserNotFoundException::new)
                .getTasks();
        tasks.sort(Comparator.comparing(Task::getCreatedAt));

        return tasks;
    }

    public Optional<Task> findById(Integer id) {
        return  taskRepository.findById(id);
    }

    public void checkAffiliation(Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName()).orElseThrow(UserNotFoundException::new);

        boolean doesTaskBelongToUser = user.getTasks().stream().anyMatch(task -> task.getId().equals(id));
        if (!doesTaskBelongToUser) {
            throw new TaskDoesNotBelongToUser();
        }
    }
    public Task createTask(TaskRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName()).orElseThrow(UserNotFoundException::new);

        Category category = categoryService.findById(request.getCategoryId()).orElseThrow(CategoryNotFoundException::new);

        Task newTask = Task.builder()
                .name(request.getName())
                .description(request.getDescription())
                .category(category)
                .user(user)
                .done(false)
                .createdAt(LocalDateTime.now())
                .build();

        return taskRepository.save(newTask);
    }

    @Transactional
    public void switchDone(Integer id) {
        Task task = taskRepository.findById(id).orElseThrow(TaskNotFoundException::new);
        task.setDone(!task.getDone());
    }

    @Transactional
    public Task updateTask(TaskRequest task, Integer id) {
        Task updatedTask = taskRepository.findById(id).orElseThrow(TaskNotFoundException::new);
        Category category = categoryService.findById(task.getCategoryId()).orElseThrow(CategoryNotFoundException::new);

        updatedTask.setName(task.getName());
        updatedTask.setDescription(task.getDescription());
        updatedTask.setCategory(category);

        return updatedTask;
    }

    public void deleteById(Integer id) {
        taskRepository.deleteById(id);
    }
}
