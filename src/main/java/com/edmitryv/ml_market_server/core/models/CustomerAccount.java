package com.edmitryv.ml_market_server.core.models;


import com.edmitryv.ml_market_server.forum.models.*;
import com.edmitryv.ml_market_server.marketplace.models.AppModel;
import com.edmitryv.ml_market_server.marketplace.models.Review;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
@Entity
public class CustomerAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne(mappedBy = "customerAccount", fetch = FetchType.LAZY)
    private User user;
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "owners", fetch = FetchType.LAZY)
    private Set<AppModel> purchasedAppModels;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "author", fetch = FetchType.LAZY)
    private Set<Review> reviews;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "author", fetch = FetchType.LAZY)
    private Set<Request> requests;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "author", fetch = FetchType.LAZY)
    private Set<RequestScore> requestsScores;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "author", fetch = FetchType.LAZY)
    private Set<Comment> comments;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "author", fetch = FetchType.LAZY)
    private Set<CommentScore> commentScores;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "author", fetch = FetchType.LAZY)
    private Set<OfferScore> offerScores;
    @ManyToMany(mappedBy = "subscribers", fetch = FetchType.LAZY)
    private Set<Request> subscriptionsRequests;
    @ManyToMany(mappedBy = "comparators", fetch = FetchType.LAZY)
    private Set<AppModel> comparableApps;
    @ManyToMany(mappedBy = "unnotifiedCustomers", fetch = FetchType.LAZY)
    private Set<Offer> uncheckedOffers;
}
