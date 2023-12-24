package ru.kopaev.todo.category;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.kopaev.todo.category.dto.CategoryRequest;
import ru.kopaev.todo.category.dto.SavedCategoryResponse;
import ru.kopaev.todo.category.exceptions.CategoryDoesNotBelongToUserException;
import ru.kopaev.todo.category.exceptions.CategoryNotFoundException;
import ru.kopaev.todo.user.User;
import ru.kopaev.todo.user.UserService;
import ru.kopaev.todo.user.exceptions.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final UserService userService;

    @GetMapping
    public List<Category> getAllCategories() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Category> categories = userService
                .findByEmail(authentication.getName())
                .orElseThrow(UserNotFoundException::new)
                .getCategories();

        categories.sort(Comparator.comparing(Category::getCreatedAt));

        return categories;
    }
    @PostMapping("/create")
    public ResponseEntity<SavedCategoryResponse> createCategory(@RequestBody CategoryRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName()).orElseThrow(UserNotFoundException::new);

        Category newCategory = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();

        Category savedCategory = categoryService.createCategory(newCategory);

        SavedCategoryResponse categoryResponse = SavedCategoryResponse.builder()
                .id(savedCategory.getId())
                .name(savedCategory.getName())
                .description(savedCategory.getDescription())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(categoryResponse);
    }
    @PutMapping("/edit")
    public ResponseEntity<String> editCategory(@RequestBody CategoryRequest request, @RequestParam Integer id) {
        categoryService.checkAffiliation(id);

        Category updatedCategory = Category.builder()
                .id(id)
                .name(request.getName())
                .description(request.getDescription())
                .build();

        categoryService.updateCategory(updatedCategory);
        return ResponseEntity.status(HttpStatus.OK).body("Category was updated!");
    }
    @DeleteMapping("/delete")
    public ResponseEntity<String>deleteCategory(@RequestParam Integer id) {
        categoryService.findById(id).orElseThrow(CategoryNotFoundException::new);
        categoryService.checkAffiliation(id);
        categoryService.deleteCategory(id);
        return ResponseEntity.status(HttpStatus.OK).body("Category was deleted!");
    }

    @ExceptionHandler
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
    }
    
    @ExceptionHandler
    public ResponseEntity<String> handleCategoryNotFoundException(CategoryNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found!");
    }

    @ExceptionHandler
    public ResponseEntity<String> handleUserNotFoundException(CategoryDoesNotBelongToUserException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This category doesn't belong ot user!");
    }
}
