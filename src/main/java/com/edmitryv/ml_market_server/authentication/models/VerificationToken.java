package com.edmitryv.ml_market_server.authentication.models;

import com.edmitryv.ml_market_server.core.models.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class VerificationToken {
    @Id
    @GeneratedValue
    private Long id;
    private String token;
    private Date expirationTime;
    private static final int EXPIRATION_TIME = 30;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public VerificationToken(String token, User user) {
        super();
        this.token = token;
        this.user = user;
        this.expirationTime = this.calcExpirationDate();
    }

    public VerificationToken(String token) {
        super();
        this.token = token;
        this.expirationTime = this.calcExpirationDate();
    }

    public Date calcExpirationDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, EXPIRATION_TIME);
        return new Date(calendar.getTime().getTime());
    }
}
