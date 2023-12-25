package ru.kopaev.todo.task;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.kopaev.todo.task.exceptions.TaskDoesNotBelongToUser;
import ru.kopaev.todo.user.User;
import ru.kopaev.todo.user.UserService;
import ru.kopaev.todo.user.exceptions.UserNotFoundException;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
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
    public void saveTask(Task task) {
        taskRepository.save(task);
    }

    public void deleteById(Integer id) {
        taskRepository.deleteById(id);
    }
}
