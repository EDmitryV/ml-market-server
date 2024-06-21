package com.edmitryv.ml_market_server.marketplace.controllers;

import com.edmitryv.ml_market_server.authentication.models.Role;
import com.edmitryv.ml_market_server.core.models.CustomerAccount;
import com.edmitryv.ml_market_server.core.models.DeveloperAccount;
import com.edmitryv.ml_market_server.core.models.Image;
import com.edmitryv.ml_market_server.core.models.User;
import com.edmitryv.ml_market_server.core.services.CustomerAccountService;
import com.edmitryv.ml_market_server.core.services.DeveloperAccountService;
import com.edmitryv.ml_market_server.core.services.ImageService;
import com.edmitryv.ml_market_server.core.services.UserService;
import com.edmitryv.ml_market_server.marketplace.dtos.*;
import com.edmitryv.ml_market_server.marketplace.models.AppModel;
import com.edmitryv.ml_market_server.marketplace.models.Review;
import com.edmitryv.ml_market_server.marketplace.models.Task;
import com.edmitryv.ml_market_server.marketplace.services.AppService;
import com.edmitryv.ml_market_server.marketplace.services.ReviewService;
import com.edmitryv.ml_market_server.marketplace.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/app")
@RequiredArgsConstructor
public class AppController {
    @Autowired
    private final AppService appService;
    @Autowired
    private final ImageService imageService;
    @Autowired
    private final TaskService taskService;
    @Autowired
    private final UserService userService;
    @Autowired
    private final ReviewService reviewService;
    @Autowired
    private final CustomerAccountService customerAccountService;
    @Autowired
    private final DeveloperAccountService developerAccountService;

    @GetMapping("/search/{text}")
    public ResponseEntity<List<AppModelListDTO>> search(@PathVariable String text
    ) {
        var l1 = appService.search(text);
        List<AppModelListDTO> appModelListDTOS = new ArrayList<>();
        for(var a : l1){
            appModelListDTOS.add(new AppModelListDTO(a));
        }
        return ResponseEntity.ok(appModelListDTOS);
    }
@PostMapping("/createReview")
public ResponseEntity<?> createReview(@RequestBody CreateReviewDTO createReviewDTO){
    try {
        Review review = new Review();
        AppModel app = appService.findById(createReviewDTO.getAppId());
        User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        review.setScore(createReviewDTO.getScore());
               review.setText(createReviewDTO.getText());
        review.setAuthor(user.getCustomerAccount());
               review.setApp(app);
               review = reviewService.saveReview(review);
               ReviewDTO reviewDTO = new ReviewDTO(review);
        return ResponseEntity.ok(reviewDTO);
    } catch (Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}

    @PostMapping("/visit/{id}")
    public ResponseEntity<?> visit(@PathVariable Long id){
        try {
            AppModel app = appService.findById(id);
            User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            if(app.getOwners().contains(user.getCustomerAccount())) return ResponseEntity.ok(null);
            Set<CustomerAccount> newOwners = new HashSet<CustomerAccount>();
            newOwners.addAll(app.getOwners());
            newOwners.add(user.getCustomerAccount());
            app.setOwners(newOwners);
            appService.save(app);
            Set<AppModel> purchased = new HashSet<>();
            purchased.addAll(user.getCustomerAccount().getPurchasedAppModels());
            purchased.add(app);
            customerAccountService.saveCustomerAccount(user.getCustomerAccount());
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/unvisit/{id}")
    public ResponseEntity<?> unvisit(@PathVariable Long id){
        try {
            AppModel app = appService.findById(id);
            User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            if(!app.getOwners().contains(user.getCustomerAccount())) return ResponseEntity.ok(null);
            Set<CustomerAccount> newOwners = new HashSet<CustomerAccount>();
            newOwners.addAll(app.getOwners());
            newOwners.remove(user.getCustomerAccount());
            app.setOwners(newOwners);
            appService.save(app);
            Set<AppModel> purchased = new HashSet<>();
            purchased.addAll(user.getCustomerAccount().getPurchasedAppModels());
            purchased.remove(app);
            customerAccountService.saveCustomerAccount(user.getCustomerAccount());
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/getAllByTaskId/{id}")
    public ResponseEntity<?> getAllByTaskId(@PathVariable Long id
    ) {
        try {
            Task task = taskService.findById(id);
            List<AppModel> apps = appService.findAllByTask(task);
            List<AppModelListDTO> appListDTOS = new ArrayList<>();
            for (AppModel app : apps) {
                appListDTOS.add(new AppModelListDTO(app));
            }
            return ResponseEntity.ok(appListDTOS);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getAllByAuthorId/{id}")
    public ResponseEntity<?> getAllByAuthorId(@PathVariable Long id
    ) {
        try {
            DeveloperAccount author = developerAccountService.findById(id);
            List<AppModel> apps = appService.findAllByAuthor(author);
            List<AppModelListDTO> appListDTOS = new ArrayList<>();
            for (AppModel app : apps) {
                appListDTOS.add(new AppModelListDTO(app));
            }
            return ResponseEntity.ok(appListDTOS);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id
    ) {
        try {
            User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            AppModel app = appService.findById(id);
            return ResponseEntity.ok(new AppModelDTO(app, user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id
    ) {
        try {
            User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            AppModel app = appService.findById(id);
            if (user.getDeveloperAccount().getDevelopedAppModels().contains(app)
                    || user.getRole() == Role.ADMIN
                    || user.getRole() == Role.MODERATOR_MARKETPLACE) {
                appService.delete(app);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().body("You can't delete this");
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CreateAppModelDTO createEditAppModelDTO) {
//        try {
            User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            Image coverImage = imageService.findById(createEditAppModelDTO.getCoverImageId());
            if (coverImage == null)
                return ResponseEntity.badRequest().body("Cover image not found");
            HashSet<Image> images = new HashSet<>();
            for (Long i : createEditAppModelDTO.getImagesIds()) {
                Image image = imageService.findById(i);
                if (image != null)
                    images.add(image);
            }
            Task task = taskService.findById(createEditAppModelDTO.getTaskId());
            if (task == null)
                return ResponseEntity.badRequest().body("Task not found");
            AppModel app = new AppModel();
            app.setName(createEditAppModelDTO.getName());
            app.setDescription(createEditAppModelDTO.getDescription());
            app.setCoverImage(coverImage);
            app.setImages(images);
            app.setTask(task);
            app.setAuthor(user.getDeveloperAccount());
            app.setPrice(createEditAppModelDTO.getPrice());
            app.setCurrency(createEditAppModelDTO.getCurrency());
            app.setCharacteristics(createEditAppModelDTO.getCharacteristics());
            app.setUrl(createEditAppModelDTO.getUrl());
            var result = appService.save(app);
            for(Image i : result.getImages()){
                i.setApp(result);
                imageService.save(i);
            }
            return ResponseEntity.ok(result);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
    }
    @PatchMapping("/update")
    public ResponseEntity<?> update(@RequestBody EditAppModelDTO editAppModelDTO) {
        try {
            User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            AppModel app = appService.findById(editAppModelDTO.getId());
            if (app == null)
                return ResponseEntity.badRequest().body("App not found");
            Image coverImage = imageService.findById(editAppModelDTO.getCoverImageId());
            if (coverImage == null)
                return ResponseEntity.badRequest().body("Cover image not found");
            HashSet<Image> images = new HashSet<>();
            for (Long i : editAppModelDTO.getImagesIds()) {
                Image image = imageService.findById(i);
                if (image != null)
                    images.add(image);
            }
            Task task = taskService.findById(editAppModelDTO.getTaskId());
            if (task == null)
                return ResponseEntity.badRequest().body("Task not found");

            app.setName(editAppModelDTO.getName());
            app.setDescription(editAppModelDTO.getDescription());
            app.setCoverImage(coverImage);
            app.setImages(images);
            app.setTask(task);
            app.setPrice(editAppModelDTO.getPrice());
            app.setCurrency(editAppModelDTO.getCurrency());
            app.setCharacteristics(editAppModelDTO.getCharacteristics());
            app = appService.save(app);
            return ResponseEntity.ok(new AppModelDTO(app, user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}