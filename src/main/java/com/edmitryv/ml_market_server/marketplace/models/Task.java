package com.edmitryv.ml_market_server.marketplace.models;

import com.edmitryv.ml_market_server.core.models.Image;
import com.edmitryv.ml_market_server.forum.models.Request;
import io.hypersistence.utils.hibernate.type.search.PostgreSQLTSVectorType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.Type;

import java.util.Set;

@Data
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String description;

    @Type(PostgreSQLTSVectorType.class)
    @Column(name = "description_search_vector",columnDefinition = "tsvector")
    private String descriptionSearchVector;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category_id", nullable=false)
    private Category category;
    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY)
    private Set<AppModel> appModels;
    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY)
    private Set<Request> requests;
}
