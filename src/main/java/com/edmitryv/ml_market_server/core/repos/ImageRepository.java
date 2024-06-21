package com.edmitryv.ml_market_server.core.repos;

import com.edmitryv.ml_market_server.core.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ImageRepository extends JpaRepository<Image, Long> {


}
