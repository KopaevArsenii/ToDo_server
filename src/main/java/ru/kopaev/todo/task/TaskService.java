package ru.kopaev.todo.task;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.kopaev.todo.category.Category;
import ru.kopaev.todo.category.CategoryRepository;
import ru.kopaev.todo.config.JwtService;
import ru.kopaev.todo.task.dto.CreateTaskRequest;
import ru.kopaev.todo.user.User;
import ru.kopaev.todo.user.UserRepository;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public void createNewTask(CreateTaskRequest request, String jwtToken) {
        System.out.println(request.getCategoryId());
        String userEmail = jwtService.extractUsername(jwtToken);
        User user = userRepository.findByEmail(userEmail).get();

        Category category = categoryRepository.findById(request.getCategoryId()).get();

        Task newTask = Task.builder()
                .name(request.getName())
                .description(request.getDescription())
                .category(category)
                .user(user)
                .build();

        taskRepository.save(newTask);
    }
}
