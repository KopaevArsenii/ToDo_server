package ru.kopaev.todo.task;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kopaev.todo.task.Task;

public interface TaskRepository extends JpaRepository<Task, Integer> {
}
