package com.edmitryv.ml_market_server.forum.repos;

import com.edmitryv.ml_market_server.forum.models.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepository extends JpaRepository<Offer, Long> {
}
