package ru.kopaev.todo.task;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public List<Task> findAllTasks(Integer id) {
        return taskRepository.findByUserId(id);
    }
    public void createTask(Task task) {
        taskRepository.save(task);
    }
}
