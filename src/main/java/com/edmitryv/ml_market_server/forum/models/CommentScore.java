package com.edmitryv.ml_market_server.forum.models;

import com.edmitryv.ml_market_server.core.models.CustomerAccount;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class CommentScore {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private CustomerAccount author;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "targetComment_id", nullable = false)
    private Comment targetComment;
    @NotNull
    private int score;
}
