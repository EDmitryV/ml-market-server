package com.edmitryv.ml_market_server.core.models;

import com.edmitryv.ml_market_server.marketplace.models.AppModel;
import jakarta.persistence.*;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class Image {
    @Id
    @GeneratedValue
    private Long id;

//    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    protected User owner;

    @OneToOne(mappedBy = "profileImage", fetch = FetchType.LAZY)
    private User profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="app_id", nullable = true)
    private AppModel app;

    @OneToOne(mappedBy = "coverImage", fetch = FetchType.LAZY)
    private AppModel coveredApp;

}
