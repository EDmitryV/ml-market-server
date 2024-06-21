package com.edmitryv.ml_market_server.forum.dtos;

import com.edmitryv.ml_market_server.core.models.User;
import com.edmitryv.ml_market_server.forum.models.Comment;
import com.edmitryv.ml_market_server.forum.models.Offer;
import com.edmitryv.ml_market_server.forum.models.Request;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RequestDTO {
    private Long id;
    private String title;
    private String description;
    private Long authorId;
    private Long authorProfileImageId;
    private String authorUsername;
    private double averageScore;
    private List<CommentDTO> comments;
    private List<OfferDTO> offers;
    private int subscribers;
    private Long taskId;

    private int myScore;
    private boolean isImSubscriber;

    public RequestDTO(Request request, User user){
        this.id = request.getId();
        this.title = request.getTitle();
        this.description = request.getDescription();
        this.authorId = request.getAuthor().getUser().getId();
        if(request.getAuthor().getUser().getProfileImage()!=null)
            this.authorProfileImageId = request.getAuthor().getUser().getProfileImage().getId();
        else
            this.authorProfileImageId=-1L;
        this.authorUsername = request.getAuthor().getUser().getUsername();
        this.averageScore = request.getAverageScore();
        this.comments = new ArrayList<>();
        for (Comment comment : request.getComments().stream().toList()) {
            this.comments.add(new CommentDTO(comment, user));
        }
        this.offers = new ArrayList<>();
        for (Offer offer : request.getOffers().stream().toList()) {
            this.offers.add(new OfferDTO(offer, user));
        }
        this.subscribers = request.getSubscribers().size();
        this.taskId = request.getTask().getId();
        var score =  user.getCustomerAccount().getRequestsScores().stream().filter(requestScore -> requestScore.getTargetRequest().getId() == request.getId())
                .findAny()
                .orElse(null);
        this.myScore = score!=null?score.getScore():0;
        var subscription = request.getSubscribers().stream().filter(customer->customer.getId() == user.getCustomerAccount().getId()).findAny().orElse(null);
        this.isImSubscriber = subscription!=null;
    }
}
