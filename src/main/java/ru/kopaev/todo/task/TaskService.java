package ru.kopaev.todo.task;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    public void createTask(Task task) {
        taskRepository.save(task);
    }
}
