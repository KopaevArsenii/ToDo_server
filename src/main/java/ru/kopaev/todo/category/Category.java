package ru.kopaev.todo.category;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kopaev.todo.user.User;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User user;
}
