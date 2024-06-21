package com.edmitryv.ml_market_server.forum.models;

import com.edmitryv.ml_market_server.core.models.CustomerAccount;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class RequestScore {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private CustomerAccount author;
    @NotNull
    private int score;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "targetRequest_id", nullable = false)
    @JsonBackReference
    private Request targetRequest;
}
