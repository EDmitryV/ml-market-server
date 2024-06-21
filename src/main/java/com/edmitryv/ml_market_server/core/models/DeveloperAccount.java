package com.edmitryv.ml_market_server.core.models;

import com.edmitryv.ml_market_server.forum.models.Offer;
import com.edmitryv.ml_market_server.marketplace.models.Answer;
import com.edmitryv.ml_market_server.marketplace.models.AppModel;
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
public class DeveloperAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne(mappedBy = "developerAccount", fetch = FetchType.LAZY)
    private User user;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "author", fetch = FetchType.LAZY)
    private Set<AppModel> developedAppModels;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "author", fetch = FetchType.LAZY)
    private Set<Answer> reviewsAnswers;
    private String about;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "author", fetch = FetchType.LAZY)
    private Set<Offer> offers;
}
