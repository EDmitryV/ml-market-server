package com.edmitryv.ml_market_server.marketplace.models;

import com.edmitryv.ml_market_server.core.models.Image;
import io.hypersistence.utils.hibernate.type.search.PostgreSQLTSVectorType;
import jakarta.persistence.*;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Type;

import java.util.Set;

@Data
@Entity
public class Category {
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
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private Set<Task> tasks;
}
