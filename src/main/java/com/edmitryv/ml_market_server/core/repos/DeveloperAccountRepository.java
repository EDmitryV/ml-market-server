package com.edmitryv.ml_market_server.core.repos;

import com.edmitryv.ml_market_server.core.models.DeveloperAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeveloperAccountRepository extends JpaRepository<DeveloperAccount, Long> {
}
