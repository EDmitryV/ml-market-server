package com.edmitryv.ml_market_server.authentication.repos;


import com.edmitryv.ml_market_server.authentication.models.Role;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository

public interface RoleRepository /*extends JpaRepository<Role, Long>*/ {
    Optional<Role> findByName(String name);
}
