package com.edmitryv.ml_market_server.forum.dtos;

import com.edmitryv.ml_market_server.forum.models.Comment;
import com.edmitryv.ml_market_server.forum.models.Offer;
import com.edmitryv.ml_market_server.forum.models.Request;
import lombok.Data;

import java.util.ArrayList;

@Data
public class RequestListDTO {
    private Long id;
    private String title;
    private Long authorId;
    private Long authorProfileImageId;
    private String authorUsername;
    private double averageScore;
    private int comments;
    private int offers;
    private int subscribers;

    public RequestListDTO(Request request){
        this.id = request.getId();
        this.title = request.getTitle();
        this.authorId = request.getAuthor().getUser().getId();
        if(request.getAuthor().getUser().getProfileImage()!=null)
            this.authorProfileImageId = request.getAuthor().getUser().getProfileImage().getId();
        else
            this.authorProfileImageId = -1L;
        this.authorUsername = request.getAuthor().getUser().getUsername();
        this.averageScore = request.getAverageScore();
        this.comments = request.getComments().stream().toList().size();
        this.offers = request.getOffers().stream().toList().size();
        this.subscribers = request.getSubscribers().size();
    }
}
