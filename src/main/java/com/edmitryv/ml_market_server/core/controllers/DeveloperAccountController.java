package com.edmitryv.ml_market_server.core.controllers;

import com.edmitryv.ml_market_server.core.dtos.UpdateUserDTO;
import com.edmitryv.ml_market_server.core.dtos.UserProfileDTO;
import com.edmitryv.ml_market_server.core.models.DeveloperAccount;
import com.edmitryv.ml_market_server.core.models.User;
import com.edmitryv.ml_market_server.core.services.DeveloperAccountService;
import com.edmitryv.ml_market_server.core.services.UserService;
import com.edmitryv.ml_market_server.forum.dtos.OfferDTO;
import com.edmitryv.ml_market_server.forum.models.Offer;
import com.edmitryv.ml_market_server.marketplace.dtos.AnswerDTO;
import com.edmitryv.ml_market_server.marketplace.dtos.AppModelListDTO;
import com.edmitryv.ml_market_server.marketplace.dtos.ReviewDTO;
import com.edmitryv.ml_market_server.marketplace.models.Answer;
import com.edmitryv.ml_market_server.marketplace.models.AppModel;
import com.edmitryv.ml_market_server.marketplace.models.Review;
import com.edmitryv.ml_market_server.marketplace.services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/developer")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeveloperAccountController {
    @Autowired
    private final UserService userService;
    @Autowired
    private final DeveloperAccountService developerAccountService;
    @Autowired
    private final ReviewService reviewService;

    @GetMapping("/{id}/apps/get-all")
    public ResponseEntity<List<AppModelListDTO>> getDevelopedApps(@PathVariable("id") Long id) {
        DeveloperAccount developer = developerAccountService.findById(id);
        if(developer == null)
            throw new RuntimeException("Developer not exists");
        List<AppModelListDTO> response = new ArrayList<>();
        for(AppModel app : developer.getDevelopedAppModels()) {
            response.add(new AppModelListDTO(app));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/me/apps/get-all")
    public ResponseEntity<List<AppModelListDTO>> getMyDevelopedApps() {
        User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        List<AppModelListDTO> response = new ArrayList<>();
        for(AppModel app : user.getDeveloperAccount().getDevelopedAppModels()) {
            response.add(new AppModelListDTO(app));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/me/answers/get-all")
    public ResponseEntity<List<AnswerDTO>> getMyAnswers() {
        User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        List<AnswerDTO> response = new ArrayList<>();
        for(Answer answer : user.getDeveloperAccount().getReviewsAnswers()) {
            response.add(new AnswerDTO(answer));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/me/offers/get-all")
    public ResponseEntity<List<OfferDTO>> getMyOffers() {
        User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        List<OfferDTO> response = new ArrayList<>();
        for(Offer offer : user.getDeveloperAccount().getOffers()) {
            response.add(new OfferDTO(offer, user));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("me/new-reviews/get-all")
    public ResponseEntity<List<ReviewDTO>> getNewReviews(){
        User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        List<ReviewDTO> response = new ArrayList<>();
        for(AppModel app : user.getDeveloperAccount().getDevelopedAppModels()) {
            for(Review review:app.getReviews())
                if(!review.isReaded()) {
                    response.add(new ReviewDTO(review));
                    review.setReaded(true);
                    reviewService.saveReview(review);
                }
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
