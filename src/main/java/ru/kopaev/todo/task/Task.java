package ru.kopaev.todo.task;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kopaev.todo.category.Category;
import ru.kopaev.todo.user.User;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String description;
    @ManyToOne()
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @ManyToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;



}
