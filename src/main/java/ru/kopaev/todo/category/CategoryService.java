package ru.kopaev.todo.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public void createNewCategory(Category category) {
        categoryRepository.save(category);
    }

    public Optional<Category> findById(Integer id) {
        return categoryRepository.findById(id);
    }
}
