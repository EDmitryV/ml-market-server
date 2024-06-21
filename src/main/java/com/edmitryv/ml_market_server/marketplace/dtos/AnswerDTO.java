package com.edmitryv.ml_market_server.marketplace.dtos;

import com.edmitryv.ml_market_server.marketplace.models.Answer;
import lombok.Data;

@Data
public class AnswerDTO {
    private Long id;
    private String text;
    private Long authorId;
    private String authorUsername;
    private Long authorProfileImageId;
    private Long reviewId;

    public AnswerDTO(Answer answer){
        this.id = answer.getId();
        this.text = answer.getText();
        this.authorId = answer.getAuthor().getUser().getId();
        this.authorUsername = answer.getAuthor().getUser().getUsername();
        this.authorProfileImageId = answer.getAuthor().getUser().getProfileImage().getId();
        this.reviewId = answer.getReview().getId();
    }
}
