package com.edmitryv.ml_market_server.forum.dtos;

import com.edmitryv.ml_market_server.core.models.User;
import com.edmitryv.ml_market_server.forum.models.Offer;
import lombok.Data;

@Data
public class OfferDTO {
    private Long id;
    private String remark;
    private Long appId;
    private String appName;
    private Long appImageId;
    private Long authorId;
    private Long authorProfileImageId;
    private String authorUsername;
    private Long targetRequestId;
    private double averageScore;

    private int myScore;

    public OfferDTO(Offer offer, User user){
        this.id = offer.getId();
        this.remark = offer.getRemark();
        this.appId = offer.getApp().getId();
        this.appName = offer.getApp().getName();
        if(offer.getApp().getCoverImage()!=null)
        this.appImageId = offer.getApp().getCoverImage().getId();
        else this.appImageId = -1L;
        this.authorId = offer.getAuthor().getUser().getId();
        if(offer.getAuthor().getUser().getProfileImage()!=null)
            this.authorProfileImageId = offer.getAuthor().getUser().getProfileImage().getId();
        else
            this.authorProfileImageId = -1L;
        this.authorUsername = offer.getAuthor().getUser().getUsername();
        this.targetRequestId = offer.getTargetRequest().getId();
        this.averageScore = offer.getAverageScore();
        if(offer.getScores()!=null) {
            var score = offer.getScores().stream().filter(offerScore -> offerScore.getAuthor().getId() == user.getCustomerAccount().getId()).findAny().orElse(null);
            this.myScore = score != null ? score.getScore() : 0;
        }else {
            this.myScore = 0;
        }
    }

}
