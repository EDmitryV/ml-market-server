package com.edmitryv.ml_market_server.marketplace.models;

import com.edmitryv.ml_market_server.core.models.CustomerAccount;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotNull
    private int score;
    private String text;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private CustomerAccount author;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_id", nullable = false)
    private AppModel app;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id", referencedColumnName = "id")
    private Answer answer;
    private boolean readed;
}
