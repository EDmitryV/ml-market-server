package com.edmitryv.ml_market_server.core.controllers;
import com.edmitryv.ml_market_server.core.models.User;
import com.edmitryv.ml_market_server.core.services.CustomerAccountService;
import com.edmitryv.ml_market_server.core.services.UserService;
import com.edmitryv.ml_market_server.forum.dtos.OfferDTO;
import com.edmitryv.ml_market_server.forum.dtos.RequestListDTO;
import com.edmitryv.ml_market_server.forum.models.Offer;
import com.edmitryv.ml_market_server.forum.models.Request;
import com.edmitryv.ml_market_server.forum.services.RequestService;
import com.edmitryv.ml_market_server.marketplace.dtos.AnswerDTO;
import com.edmitryv.ml_market_server.marketplace.dtos.AppModelListDTO;
import com.edmitryv.ml_market_server.marketplace.dtos.ReviewDTO;
import com.edmitryv.ml_market_server.marketplace.models.Answer;
import com.edmitryv.ml_market_server.marketplace.models.AppModel;
import com.edmitryv.ml_market_server.marketplace.models.Review;
import com.edmitryv.ml_market_server.marketplace.services.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping(value = "/customer")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerAccountController {
    @Autowired
    private final UserService userService;
    @Autowired
    private final RequestService requestService;
    @Autowired
    private final CustomerAccountService customerAccountService;
    @Autowired
    private final AnswerService answerService;

    @GetMapping("/me/apps/get-all")
    public ResponseEntity<List<AppModelListDTO>> getMyPurchasedApps() {
        User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        List<AppModelListDTO> response = new ArrayList<>();
        for(AppModel app : user.getCustomerAccount().getPurchasedAppModels()) {
            response.add(new AppModelListDTO(app));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/me/reviews/get-all")
    public ResponseEntity<List<ReviewDTO>> getMyReviews() {
        User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        List<ReviewDTO> response = new ArrayList<>();
        for(Review review : user.getCustomerAccount().getReviews()) {
            response.add(new ReviewDTO(review));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/me/requests/get-all")
    public ResponseEntity<List<RequestListDTO>> getMyRequests() {
        User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        HashSet<RequestListDTO> response = new HashSet<>();
        for(Request request : user.getCustomerAccount().getRequests())
            response.add(new RequestListDTO(request));
        for(Request request : user.getCustomerAccount().getSubscriptionsRequests())
            response.add(new RequestListDTO(request));
        return new ResponseEntity<>(response.stream().toList(), HttpStatus.OK);
    }

    @GetMapping("/me/new-offers/get-all")
    public ResponseEntity<List<OfferDTO>> getNewOffers() {
        User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        List<OfferDTO> response = new ArrayList<>();
        List<Offer> offers = new ArrayList<>();
        for(Offer offer : user.getCustomerAccount().getUncheckedOffers()) {
            response.add(new OfferDTO(offer, user));
        }
        user.getCustomerAccount().setUncheckedOffers(new HashSet<>());
        customerAccountService.saveCustomerAccount(user.getCustomerAccount());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/me/new-answers/get-all")
    public ResponseEntity<List<AnswerDTO>> getNewAnswers() {
        User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        List<AnswerDTO> response = new ArrayList<>();
        for(Review review : user.getCustomerAccount().getReviews()) {
            if(review.getAnswer()!=null && !review.getAnswer().isReaded()){
                response.add(new AnswerDTO(review.getAnswer()));
                review.getAnswer().setReaded(true);
                answerService.saveAnswer(review.getAnswer());
            }

        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}