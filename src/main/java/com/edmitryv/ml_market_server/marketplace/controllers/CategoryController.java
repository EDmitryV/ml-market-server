package com.edmitryv.ml_market_server.marketplace.controllers;

import com.edmitryv.ml_market_server.authentication.mail.RegistrationCompleteEvent;
import com.edmitryv.ml_market_server.authentication.models.RegisterRequest;
import com.edmitryv.ml_market_server.authentication.repos.VerificationTokenRepository;
import com.edmitryv.ml_market_server.authentication.services.AuthenticationService;
import com.edmitryv.ml_market_server.core.models.User;
import com.edmitryv.ml_market_server.core.services.UserService;
import com.edmitryv.ml_market_server.marketplace.dtos.CategoryDTO;
import com.edmitryv.ml_market_server.marketplace.models.Category;
import com.edmitryv.ml_market_server.marketplace.models.Task;
import com.edmitryv.ml_market_server.marketplace.services.CategoryService;
import com.edmitryv.ml_market_server.marketplace.services.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    @Autowired
    private final CategoryService categoryService;
    @Autowired
    private final TaskService taskService;

    @GetMapping("/search/{text}")
    public ResponseEntity<List<CategoryDTO>> search(@PathVariable String text
    ) {
        return ResponseEntity.ok(categoryService.search(text));
    }
@GetMapping("/getByTaskId/{id}")
public ResponseEntity<CategoryDTO> getByTaskId(@PathVariable long id){
        Task task = taskService.findById(id);
        Category category = task.getCategory();
        category.setTasks(new HashSet<Task>(taskService.findAllByCategoryId(category.getId())));
        CategoryDTO categoryDTO = new CategoryDTO(category);
        return ResponseEntity.ok(categoryDTO);
}
    @GetMapping("/getAll")
    public ResponseEntity<?> getAll(
    ) {
        try {
            List<Category> categories = categoryService.getAllCategories();
            List<CategoryDTO> categoryDTOS = new ArrayList<>();
            for (Category value : categories) {
                value.setTasks(new HashSet<Task>(taskService.findAllByCategoryId(value.getId())));
                categoryDTOS.add(new CategoryDTO(value));
            }
            return ResponseEntity.ok(categoryDTOS);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
