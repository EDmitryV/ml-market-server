package com.edmitryv.ml_market_server.forum.models;

import com.edmitryv.ml_market_server.core.models.CustomerAccount;
import com.edmitryv.ml_market_server.core.models.DeveloperAccount;
import com.edmitryv.ml_market_server.marketplace.models.AppModel;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String remark;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_id", nullable = false)
    private AppModel app;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authorId", nullable = false)
    private DeveloperAccount author;
    @OneToMany(mappedBy = "targetOffer", fetch = FetchType.LAZY)
    private Set<OfferScore> scores;
    @Transient
    private double averageScore;
    @OneToMany(mappedBy = "targetOffer", fetch = FetchType.LAZY)
    private Set<Comment> comments;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "targetRequest_id", nullable = false)
    @JsonBackReference
    private Request targetRequest;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "offer_customer",
            joinColumns = @JoinColumn(name = "offer_id"),
            inverseJoinColumns = @JoinColumn(name = "customer_id"))
    private Set<CustomerAccount> unnotifiedCustomers;

    @PostLoad
    private void onLoad() {

        double scores = 0;
        List<OfferScore> offerScoresList = this.scores.stream().toList();
        for (OfferScore offerScore : offerScoresList) {
            scores += offerScore.getScore();
        }
        scores = !offerScoresList.isEmpty() ? scores / offerScoresList.size() : 0.0;
        scores = Math.floor(scores*100);
        this.averageScore = scores/100;
    }
}
