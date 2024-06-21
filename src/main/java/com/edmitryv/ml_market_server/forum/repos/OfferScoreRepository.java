package com.edmitryv.ml_market_server.forum.repos;

import com.edmitryv.ml_market_server.core.models.CustomerAccount;
import com.edmitryv.ml_market_server.forum.models.Offer;
import com.edmitryv.ml_market_server.forum.models.OfferScore;
import com.edmitryv.ml_market_server.forum.models.Request;
import com.edmitryv.ml_market_server.forum.models.RequestScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfferScoreRepository extends JpaRepository<OfferScore, Long> {
    List<OfferScore> findByTargetOfferAndAuthor(Offer targetOffer, CustomerAccount author);
}
