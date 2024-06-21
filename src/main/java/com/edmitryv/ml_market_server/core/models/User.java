package com.edmitryv.ml_market_server.core.models;

import com.edmitryv.ml_market_server.authentication.models.Role;
import com.edmitryv.ml_market_server.authentication.models.Status;
import com.edmitryv.ml_market_server.authentication.models.Token;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@Builder
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue
    protected Long id;
    public User(String username, String email, String password, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.registrationDate = ZonedDateTime.now();
    }

    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    private String email;

    @OneToOne( cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "profileImage_id", referencedColumnName = "id")
    private Image profileImage;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner", fetch = FetchType.LAZY)
    private Set<Image> images;

    private ZonedDateTime registrationDate;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    private List<Token> tokens;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Status status = Status.NORMAL;

    @Column(name = "enabled")
    private boolean enabled = false;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "developerAccount_id", referencedColumnName = "id")
    private DeveloperAccount developerAccount;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "customerAccount_id", referencedColumnName = "id")
    private CustomerAccount customerAccount;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return status == Status.NORMAL;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status == Status.NORMAL;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return status == Status.NORMAL;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}