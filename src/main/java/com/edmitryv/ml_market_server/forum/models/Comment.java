package com.edmitryv.ml_market_server.forum.models;

import com.edmitryv.ml_market_server.core.models.CustomerAccount;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotNull
    private String text;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="targetComment_id", nullable=true)
    private Comment targetComment;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="targetOffer_id", nullable=true)
    private Offer targetOffer;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "targetComment", fetch = FetchType.LAZY)
    private Set<Comment> comments;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="author_id", nullable=false)
    private CustomerAccount author;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "targetComment", fetch = FetchType.LAZY)
    private Set<CommentScore> scores;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "targetRequest_id", nullable = false)
    @JsonBackReference
    private Request targetRequest;
    @Transient
    private double averageScore;
    @PostLoad
    private void onLoad() {

        double scores = 0;
        List<CommentScore> commentScoresList = this.scores.stream().toList();
        for (int i = 0; i < commentScoresList.size(); i++) {
            scores += commentScoresList.get(i).getScore();
        }
        scores = !commentScoresList.isEmpty() ? scores / commentScoresList.size() : 0.0;
        scores = Math.floor(scores*100);
        this.averageScore = scores/100;
    }
}
