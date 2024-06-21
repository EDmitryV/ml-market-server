package com.edmitryv.ml_market_server.forum.controllers;

import com.edmitryv.ml_market_server.core.models.CustomerAccount;
import com.edmitryv.ml_market_server.core.models.User;
import com.edmitryv.ml_market_server.core.services.CustomerAccountService;
import com.edmitryv.ml_market_server.core.services.UserService;
import com.edmitryv.ml_market_server.forum.dtos.*;
import com.edmitryv.ml_market_server.forum.models.*;
import com.edmitryv.ml_market_server.forum.services.CommentService;
import com.edmitryv.ml_market_server.forum.services.OfferService;
import com.edmitryv.ml_market_server.forum.services.RequestService;
import com.edmitryv.ml_market_server.marketplace.models.AppModel;
import com.edmitryv.ml_market_server.marketplace.models.Task;
import com.edmitryv.ml_market_server.marketplace.services.AppService;
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
@RequestMapping("/request")
@RequiredArgsConstructor
public class RequestController {
    @Autowired
    private final RequestService requestService;
    @Autowired
    private final TaskService taskService;
    @Autowired
    private final UserService userService;
    @Autowired
    private final AppService appService;
    @Autowired
    private final OfferService offerService;
    @Autowired
    private final CommentService commentService;
    @Autowired
    private final CustomerAccountService customerAccountService;


    @GetMapping("/search/{text}")
    public ResponseEntity<List<RequestListDTO>> search(@PathVariable String text
    ) {
        return ResponseEntity.ok(requestService.search(text));
    }

    @GetMapping("/getAllByTaskId/{id}")
    public ResponseEntity<?> getAllByTaskId(@PathVariable Long id
    ) {
        try {
            Task task = taskService.findById(id);
            List<Request> requests = requestService.findAllByTask(task);
            List<RequestListDTO> requestListDTOS = new ArrayList<>();
            for (Request request : requests) {
                requestListDTOS.add(new RequestListDTO(request));
            }
            return ResponseEntity.ok(requestListDTOS);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getMyScoreByRequestId/{id}")
    public ResponseEntity<Integer> getMyScoreByRequestId(@PathVariable Long id){
            Request request = requestService.findById(id);
            User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            RequestScore requestScore = requestService.findScoreByRequestAndAuthor(request, user.getCustomerAccount());
            if(requestScore != null)
                return ResponseEntity.ok(requestScore.getScore());
            else
                return ResponseEntity.ok(0);
    }

    @PostMapping("/setMyScoreByRequestId/{id}")
    public ResponseEntity<RequestDTO> setMyScore(@PathVariable Long id, @RequestBody int score){
        Request request = requestService.findById(id);
        User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        RequestScore requestScore = new RequestScore();
        requestScore.setScore(score);
        requestScore.setTargetRequest(request);
        requestScore.setAuthor(user.getCustomerAccount());
        requestService.saveRequestScore(requestScore);
        return ResponseEntity.ok(new RequestDTO(request, user));
    }

    @PostMapping("/setMyScoreByOfferId/{id}")
    public ResponseEntity<RequestDTO> setMyOfferScore(@PathVariable Long id, @RequestBody int score){
        Offer offer = offerService.findById(id);
        User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        OfferScore offerScore = new OfferScore();
        offerScore.setScore(score);
        offerScore.setTargetOffer(offer);
        offerScore.setAuthor(user.getCustomerAccount());
        offerService.saveOfferScore(offerScore);
        return ResponseEntity.ok(new RequestDTO(offer.getTargetRequest(), user));
    }

    @PostMapping("/setMyScoreByCommentId/{id}")
    public ResponseEntity<RequestDTO> setMyCommentScore(@PathVariable Long id, @RequestBody int score){
        Comment comment = commentService.findById(id);
        User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        CommentScore commentScore = new CommentScore();
        commentScore.setScore(score);
        commentScore.setTargetComment(comment);
        commentScore.setAuthor(user.getCustomerAccount());
        commentService.saveCommentScore(commentScore);
        return ResponseEntity.ok(new RequestDTO(comment.getTargetRequest(), user));
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id
    ) {
        try {
            Request request = requestService.findById(id);
            User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            if(request == null){
                return ResponseEntity.badRequest().body("Request not found");
            }
            return ResponseEntity.ok(new RequestDTO(request, user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CreateRequestDTO editRequestDTO) {
        try {
            User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

            Task task = taskService.findById(editRequestDTO.getTaskId());
            if (task == null)
                return ResponseEntity.badRequest().body("Task not found");
            Request request = new Request();
            request.setTitle(editRequestDTO.getTitle());
            request.setTask(task);
            request.setAuthor(user.getCustomerAccount());
            request.setDescription(editRequestDTO.getDescription());
            return ResponseEntity.ok(requestService.save(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<?> update(@RequestBody EditRequestDTO editRequestDTO) {
        try {
            User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            Request request =requestService.findById(editRequestDTO.getId());
            if(request == null)
                throw new RuntimeException("Request not found");
            if(request.getAuthor().getId() != user.getCustomerAccount().getId())
                throw new RuntimeException("Access rejected");

            request.setTitle(editRequestDTO.getTitle());
            request.setDescription(editRequestDTO.getDescription());
            Task task = taskService.findById(editRequestDTO.getTaskId());
            if(task != null)
                request.setTask(task);
            requestService.save(request);
            return ResponseEntity.ok(new RequestDTO(request, user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id){
        User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Request request = requestService.findById(id);
        if(request == null)
            throw new RuntimeException("Request not found");
        if(request.getAuthor().getId()!=user.getCustomerAccount().getId())
            throw new RuntimeException("Access rejected");
        requestService.deleteRequestById(id);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/createOffer")
    public ResponseEntity<OfferDTO> createOffer (@RequestBody CreateOfferDTO offerDTO){
        User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Request request = requestService.findById(offerDTO.getTargetRequestId());
        AppModel app = appService.findById(offerDTO.getAppId());
        Offer offer = new Offer();
        offer.setApp(app);
        offer.setAuthor(user.getDeveloperAccount());
        offer.setRemark(offerDTO.getRemark());
//        offer.setUnnotifiedCustomers(request.getSubscribers());
        for(CustomerAccount customer : request.getSubscribers()){
            Set<Offer> newOffers = new HashSet<>();
            newOffers.addAll(customer.getUncheckedOffers());
            newOffers.add(offer);
            customer.setUncheckedOffers(newOffers);
            customerAccountService.saveCustomerAccount(customer);
        }
        Set<Offer> newOffers = new HashSet<>();
        newOffers.addAll(request.getAuthor().getUncheckedOffers());
        newOffers.add(offer);
        request.getAuthor().setUncheckedOffers(newOffers);
        customerAccountService.saveCustomerAccount(request.getAuthor());

        offer.setTargetRequest(request);
        offer.setScores(new HashSet<>());
        offer = offerService.saveOffer(offer);
        List<Offer> offers = new ArrayList<>();
        offers.addAll(request.getOffers());
        offers.add(offer);
        request.setOffers(offers);
        requestService.save(request);
        return ResponseEntity.ok(new OfferDTO(offer, user));
    }

    @DeleteMapping("/deleteOfferById/{id}")
    public ResponseEntity<?> deleteOffer(@PathVariable Long id){

        User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        var offer = offerService.findById(id);
        if(user.getDeveloperAccount().getId()!=offer.getAuthor().getId())
            throw new RuntimeException("No access");
        offerService.deleteOfferById(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/getMyScoresByOfferId/{id}")
    public ResponseEntity<Integer> getScoresByOfferId(@PathVariable Long id){
        Offer offer = offerService.findById(id);
        User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        OfferScore offerScore = offerService.findScoreByOfferAndAuthor(offer, user.getCustomerAccount());
        if(offerScore != null)
            return ResponseEntity.ok(offerScore.getScore());
        else
            return ResponseEntity.ok(0);
    }

    @GetMapping("/getMyScoresByCommentId/{id}")
    public ResponseEntity<Integer> getScoresByCommentId(@PathVariable Long id){
        Comment comment = commentService.findById(id);
        User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        CommentScore commentScore = commentService.findScoreByCommentAndAuthor(comment, user.getCustomerAccount());
        if(commentScore != null)
            return ResponseEntity.ok(commentScore.getScore());
        else
            return ResponseEntity.ok(0);
    }



    @PostMapping("/createComment")
    public ResponseEntity<CommentDTO> createComment(@RequestBody CreateCommentDTO createCommentDTO){
        Comment newComment = new Comment();
        User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        newComment.setAuthor(user.getCustomerAccount());
        Request request = requestService.findById(createCommentDTO.getTargetRequestId());
        newComment.setTargetRequest(request);
        Comment comment = commentService.findById(createCommentDTO.getTargetCommentId());
        newComment.setTargetComment(comment);
        Offer offer = offerService.findById(createCommentDTO.getTargetOfferId());
        newComment.setTargetOffer(offer);
        newComment.setText(createCommentDTO.getText());
        return ResponseEntity.ok(new CommentDTO(commentService.saveComment(newComment), user));
    }

    @PatchMapping("/updateComment")
    public ResponseEntity<CommentDTO> updateComment(@RequestBody UpdateCommentDTO updateCommentDTO){
        Comment newComment = commentService.findById(updateCommentDTO.getId());
        User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(user.getCustomerAccount().getId()!=newComment.getAuthor().getId())
            throw new RuntimeException("No access");
        newComment.setAuthor(user.getCustomerAccount());
        newComment.setText(updateCommentDTO.getText());
        return ResponseEntity.ok(new CommentDTO(commentService.saveComment(newComment), user));
    }

    @DeleteMapping("/deleteCommentById/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id){
        Comment comment = commentService.findById(id);
        User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(user.getCustomerAccount().getId()!=comment.getAuthor().getId())
            throw new RuntimeException("No access");
        commentService.deleteCommentById(id);
        return ResponseEntity.ok(null);
    }

    @PatchMapping("/subscribeByRequestId/{id}")
    public ResponseEntity<RequestDTO> subscribe(@PathVariable Long id){
        User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Request request = requestService.findById(id);
        if(user.getCustomerAccount().getSubscriptionsRequests().contains(request)) return ResponseEntity.ok(new RequestDTO(request, user));
        Set<Request> subscriptions = new HashSet<>();
        subscriptions.addAll(user.getCustomerAccount().getSubscriptionsRequests());
        subscriptions.add(request);
        user.getCustomerAccount().setSubscriptionsRequests(subscriptions);
        customerAccountService.saveCustomerAccount(user.getCustomerAccount());
        if(request.getSubscribers().contains(user.getCustomerAccount())) return ResponseEntity.ok(new RequestDTO(request, user));
        Set<CustomerAccount> subscribers = new HashSet<>();
        subscribers.addAll(request.getSubscribers());
        subscribers.add(user.getCustomerAccount());
        request.setSubscribers(subscribers);
        request = requestService.save(request);

        return ResponseEntity.ok(new RequestDTO(request, user));
    }

    @PatchMapping("/unsubscribeByRequestId/{id}")
    public ResponseEntity<RequestDTO> unsubscribe(@PathVariable Long id){
        User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Request request = requestService.findById(id);
        Set<CustomerAccount> subscribers = new HashSet<>();
        subscribers.addAll(request.getSubscribers());
        subscribers.remove(user.getCustomerAccount());
        request.setSubscribers(subscribers);
        requestService.save(request);
        Set<Request> subscriptions = new HashSet<>();
        subscriptions.addAll(user.getCustomerAccount().getSubscriptionsRequests());
        subscriptions.remove(request);
        user.getCustomerAccount().setSubscriptionsRequests(subscriptions);
        customerAccountService.saveCustomerAccount(user.getCustomerAccount());
        return ResponseEntity.ok(new RequestDTO(requestService.save(request), user));
    }
}
