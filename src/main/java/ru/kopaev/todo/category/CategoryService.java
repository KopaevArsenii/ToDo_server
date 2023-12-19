package ru.kopaev.todo.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kopaev.todo.category.dto.CreateCategoryRequest;
import ru.kopaev.todo.config.JwtService;
import ru.kopaev.todo.user.User;
import ru.kopaev.todo.user.UserRepository;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public void createNewCategory(CreateCategoryRequest request, String jwtToken) {
        String userEmail = jwtService.extractUsername(jwtToken);
        System.out.println(userEmail);
        User user = userRepository.findByEmail(userEmail).get();
        System.out.println(user);

        Category newCategory = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .user(user)
                .build();
        categoryRepository.save(newCategory);
    }
}
