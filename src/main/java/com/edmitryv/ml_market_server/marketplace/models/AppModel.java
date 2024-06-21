package com.edmitryv.ml_market_server.marketplace.models;

import com.edmitryv.ml_market_server.core.models.CustomerAccount;
import com.edmitryv.ml_market_server.core.models.DeveloperAccount;
import com.edmitryv.ml_market_server.core.models.Image;
import com.edmitryv.ml_market_server.forum.models.Offer;
import io.hypersistence.utils.hibernate.type.search.PostgreSQLTSVectorType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class AppModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String url;
    @NotNull
    @Column(columnDefinition="TEXT", length = 2000)
    private String description;

    @Type(PostgreSQLTSVectorType.class)
    @Column(name = "description_search_vector",columnDefinition = "tsvector")
    private String descriptionSearchVector;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coverImage_id", nullable = false)
    private Image coverImage;
    @OneToMany(mappedBy = "app", fetch = FetchType.LAZY)
    private Set<Image> images;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "app_owner", joinColumns = @JoinColumn(name = "appModel_id"),
            inverseJoinColumns = @JoinColumn(name = "customerAccount_id"))
    private Set<CustomerAccount> owners;
    @Transient
    private int downloads;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private DeveloperAccount author;
    @NotNull
    private double price;
    @NotNull
    private String currency;
    @OneToMany(mappedBy = "app", fetch = FetchType.LAZY)
    private Set<Review> reviews;
    @Transient
    private double scores;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "app", fetch = FetchType.LAZY)
    private Set<Offer> offers;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "app_comparator",
            joinColumns = @JoinColumn(name = "app_id"),
            inverseJoinColumns = @JoinColumn(name = "comparator_id"))
    private Set<CustomerAccount> comparators;
    private String characteristics;

    @PostLoad
    private void onLoad() {
        this.downloads = owners.size();
        double scores = 0;
        List<Review> reviewsList = reviews.stream().toList();
        for (Review review : reviewsList) {
            scores += review.getScore();
        }
        scores = !reviewsList.isEmpty() ? scores / reviewsList.size() : 0.0;
        scores = scores * 100;
        scores = Math.floor(scores);
        this.scores = scores / 100;
    }

//    public static Specification<AppModel> search(String search) {
//        return (root, query, cb) -> {
//            if (search == null || search.isBlank()) return null;
//
//            return cb.isTrue(
//                    cb.function(
//                            "tsvector_match",
//                            Boolean.class,
//                            root.get(AppModel_.descriptionSearchVector),
//                            cb.function(
//                                    "plainto_tsquery", String.class, cb.literal(search)
//                            )
//                    )
//            );
//        };
//    }
}