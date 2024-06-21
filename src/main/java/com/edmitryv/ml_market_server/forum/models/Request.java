package com.edmitryv.ml_market_server.forum.models;

import com.edmitryv.ml_market_server.core.models.CustomerAccount;
import com.edmitryv.ml_market_server.marketplace.models.Task;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.Type;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;
import io.hypersistence.utils.hibernate.type.search.PostgreSQLTSVectorType;
@Data
@Entity
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotNull
    private String title;
    @NotNull
    @Column(columnDefinition="TEXT", length = 2000)
    private String description;

    @Type(PostgreSQLTSVectorType.class)
    @Column(name = "description_search_vector",columnDefinition = "tsvector")
    private String descriptionSearchVector;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private CustomerAccount author;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "request_subscriber",
            joinColumns = @JoinColumn(name = "request_id"),
            inverseJoinColumns = @JoinColumn(name = "subscriber_id"))
    private Set<CustomerAccount> subscribers;
    @Transient
    private double averageScore;
    @OneToMany(cascade = CascadeType.DETACH, mappedBy = "targetRequest", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<RequestScore> scores;
    @OneToMany(cascade = CascadeType.DETACH, mappedBy = "targetRequest", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Comment> comments;
    @OneToMany(cascade = CascadeType.DETACH, mappedBy = "targetRequest", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Offer> offers;


    @PostLoad
    private void onLoad() {

        double scores = 0;
        List<RequestScore> requestScoresList = this.scores.stream().toList();
        for (RequestScore requestScore : requestScoresList) {
            scores += requestScore.getScore();
        }
        scores = !requestScoresList.isEmpty() ? scores / requestScoresList.size() : 0.0;
        scores = Math.floor(scores*100);
        this.averageScore = scores/100;
    }
}
