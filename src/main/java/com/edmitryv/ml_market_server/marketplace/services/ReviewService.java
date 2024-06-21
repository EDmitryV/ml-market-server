package com.edmitryv.ml_market_server.marketplace.services;

import com.edmitryv.ml_market_server.core.models.CustomerAccount;
import com.edmitryv.ml_market_server.marketplace.models.AppModel;
import com.edmitryv.ml_market_server.marketplace.models.Review;
import com.edmitryv.ml_market_server.marketplace.repos.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    public List<Review> findAllByApp(AppModel app) {
        return reviewRepository.findAllByApp(app);
    }
    public List<Review> findAllByAuthor(CustomerAccount author){
        return reviewRepository.findAllByAuthor(author);
    }

    public Review findById(Long id){
        return reviewRepository.findById(id).orElse(null);
    }

    public Review saveReview(Review review){
        return reviewRepository.save(review);
    }

    public void deleteReviewById(Long id){
        reviewRepository.deleteById(id);
    }

}
