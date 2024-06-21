package com.edmitryv.ml_market_server.marketplace.models;

import com.edmitryv.ml_market_server.core.models.DeveloperAccount;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotNull
    private String text;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private DeveloperAccount author;
    @OneToOne(mappedBy = "answer", fetch = FetchType.LAZY)
    private Review review;
    private boolean readed;
}
