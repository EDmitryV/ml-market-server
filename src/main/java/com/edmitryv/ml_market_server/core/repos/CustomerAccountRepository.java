package com.edmitryv.ml_market_server.core.repos;

import com.edmitryv.ml_market_server.core.models.CustomerAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerAccountRepository extends JpaRepository<CustomerAccount, Long> {
}
