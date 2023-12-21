package ru.kopaev.todo.category;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.kopaev.todo.category.dto.CreateCategoryRequest;
import ru.kopaev.todo.category.dto.EditCategoryRequest;
import ru.kopaev.todo.category.exceptions.CategoryDoesNotBelongToUserException;
import ru.kopaev.todo.category.exceptions.CategoryNotFoundException;
import ru.kopaev.todo.config.JwtService;
import ru.kopaev.todo.user.User;
import ru.kopaev.todo.user.UserService;
import ru.kopaev.todo.user.exceptions.UserNotFoundException;

import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final JwtService jwtService;
    private final UserService userService;

    @GetMapping
    public List<Category> getAllCategories(@RequestHeader(HttpHeaders.AUTHORIZATION) String header) {
        String token = jwtService.extractTokenFromHeader(header);
        String userEmail = jwtService.extractUsername(token);

        User user = userService.findByEmail(userEmail).orElseThrow(UserNotFoundException::new);

        return user.getCategories();
    }
    @PostMapping("/create")
    public ResponseEntity<String> createCategory(@RequestBody CreateCategoryRequest request, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {
        String token = jwtService.extractTokenFromHeader(header);
        String userEmail = jwtService.extractUsername(token);

        User user = userService.findByEmail(userEmail).orElseThrow(UserNotFoundException::new);

        Category newCategory = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .user(user)
                .build();

        categoryService.createCategory(newCategory);

        return ResponseEntity.status(HttpStatus.CREATED).body("New category was created!");
    }
    @PutMapping("/edit")
    public ResponseEntity<String> editCategory(@RequestBody EditCategoryRequest request, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {
        String token = jwtService.extractTokenFromHeader(header);
        String userEmail = jwtService.extractUsername(token);

        User user = userService.findByEmail(userEmail).orElseThrow(UserNotFoundException::new);
        List<Category> userCategories = user.getCategories();
        Stream<Category> stream = userCategories.stream();
        boolean doesCategoryBelongToUser = stream.anyMatch(category -> category.getId() == request.getId());
        if (!doesCategoryBelongToUser) {
            throw new CategoryDoesNotBelongToUserException();
        }

        Category category = categoryService.findById(request.getId()).orElseThrow(CategoryNotFoundException::new);
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        categoryService.updateCategory(category);
        return ResponseEntity.status(HttpStatus.OK).body("Category was updated!");
    }
    @DeleteMapping("/delete")
    public ResponseEntity<String>deleteCategory(@RequestParam Integer id) {
        //ToDo: rewrite all dumb token extraction to this way;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userService.findByEmail(authentication.getName()).orElseThrow(UserNotFoundException::new);
        List<Category> userCategories = user.getCategories();
        Stream<Category> stream = userCategories.stream();
        boolean doesCategoryBelongToUser = stream.anyMatch(category -> category.getId() == id);
        if (!doesCategoryBelongToUser) {
            throw new CategoryDoesNotBelongToUserException();
        }

        categoryService.findById(id).orElseThrow(CategoryNotFoundException::new);

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
