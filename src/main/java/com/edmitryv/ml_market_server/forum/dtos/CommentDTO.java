package com.edmitryv.ml_market_server.forum.dtos;

import com.edmitryv.ml_market_server.core.models.User;
import com.edmitryv.ml_market_server.forum.models.Comment;
import lombok.Data;

@Data
public class CommentDTO {
    private Long id;
    private String text;
    private double averageScore;
    private Long authorId;
    private String authorUsername;
    private Long authorProfileImageId;
    private Long targetRequestId;
    private Long targetCommentId;
    private Long targetOffer;
    private int myScore;


    public CommentDTO(Comment comment, User user) {
        this.id = comment.getId();
        this.text = comment.getText();
        this.averageScore = comment.getAverageScore();
        this.authorId = comment.getAuthor().getUser().getId();
        this.authorUsername = comment.getAuthor().getUser().getUsername();
        if(comment.getAuthor().getUser().getProfileImage()!=null)
            this.authorProfileImageId = comment.getAuthor().getUser().getProfileImage().getId();
        else
            this.authorProfileImageId = -1L;
        this.targetRequestId = comment.getTargetRequest().getId();
        if(comment.getTargetComment() != null)
            this.targetCommentId = comment.getTargetComment().getId();
        else
            this.targetCommentId = -1L;
        if(comment.getTargetOffer()!=null)
            this.targetOffer = comment.getTargetOffer().getId();
        else
            this.targetOffer = -1L;
        if (comment.getScores()!=null) {
            var score = comment.getScores().stream().filter(commentScore -> commentScore.getAuthor().getId() == user.getCustomerAccount().getId()).findAny().orElse(null);
            this.myScore = score != null ? score.getScore() : 0;
        }else{
            this.myScore = 0;
        }
    }
}
