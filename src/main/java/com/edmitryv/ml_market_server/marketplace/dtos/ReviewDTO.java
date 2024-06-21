package com.edmitryv.ml_market_server.marketplace.dtos;

import com.edmitryv.ml_market_server.marketplace.models.Review;
import lombok.Data;

@Data
public class ReviewDTO {
    private Long id;
    private int score;
    private String text;
    private Long authorId;
    private String authorUsername;
    private Long authorProfileImageId;
    private Long appId;
    private AnswerDTO answer;

    public ReviewDTO(Review review){
        this.id = review.getId();
        this.score = review.getScore();
        this.text = review.getText();
        this.authorId = review.getAuthor().getUser().getId();
        this.authorUsername = review.getAuthor().getUser().getUsername();
        if(review.getAuthor().getUser().getProfileImage()!=null)
            this.authorProfileImageId = review.getAuthor().getUser().getProfileImage().getId();
        else
            this.authorProfileImageId = null;
        this.appId = review.getApp().getId();
        if(this.answer!=null)
            this.answer = new AnswerDTO(review.getAnswer());
        else this.answer = null;
    }
}
