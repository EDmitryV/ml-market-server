package com.edmitryv.ml_market_server.core.repos;

import com.edmitryv.ml_market_server.core.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsernameOrEmail(String username, String email);
    List<User> findByUsernameContaining(String name);
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
}
