package ru.kopaev.todo.category;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.kopaev.todo.category.exceptions.CategoryDoesNotBelongToUserException;
import ru.kopaev.todo.category.exceptions.CategoryNotFoundException;
import ru.kopaev.todo.user.User;
import ru.kopaev.todo.user.UserService;
import ru.kopaev.todo.user.exceptions.UserNotFoundException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserService userService;

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Transactional
    public void updateCategory(Category category) {
        Category oldCategory = categoryRepository.findById(category.getId()).orElseThrow(CategoryNotFoundException::new);
        oldCategory.setName(category.getName());
        oldCategory.setDescription(category.getDescription());
    }

    public void deleteCategory(Integer id) {
        categoryRepository.deleteById(id);
    }

    public Optional<Category> findById(Integer id) {
        return categoryRepository.findById(id);
    }

    public void checkAffiliation(Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName()).orElseThrow(UserNotFoundException::new);

        boolean doesCategoryBelongToUser = user.getCategories().stream().anyMatch(category -> category.getId().equals(id));
        if (!doesCategoryBelongToUser) {
            throw new CategoryDoesNotBelongToUserException();
        }
    }
}
