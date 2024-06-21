package com.edmitryv.ml_market_server.forum.repos;

import com.edmitryv.ml_market_server.core.models.CustomerAccount;
import com.edmitryv.ml_market_server.forum.models.Request;
import com.edmitryv.ml_market_server.forum.models.RequestScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestScoreRepository extends JpaRepository<RequestScore, Long> {
    List<RequestScore> findByTargetRequestAndAuthor(Request targetRequest, CustomerAccount author);
}
