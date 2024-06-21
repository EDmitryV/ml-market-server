package com.edmitryv.ml_market_server.marketplace.repos;

import com.edmitryv.ml_market_server.core.models.DeveloperAccount;
import com.edmitryv.ml_market_server.marketplace.models.Answer;
import com.edmitryv.ml_market_server.marketplace.models.AppModel;
import com.edmitryv.ml_market_server.marketplace.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Answer findByReview(Review review);

    List<Answer> findAllByAuthor(DeveloperAccount author);
}
