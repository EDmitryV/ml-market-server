package com.edmitryv.ml_market_server.marketplace.services;

import com.edmitryv.ml_market_server.core.models.CustomerAccount;
import com.edmitryv.ml_market_server.core.models.DeveloperAccount;
import com.edmitryv.ml_market_server.marketplace.models.AppModel;
import com.edmitryv.ml_market_server.marketplace.models.Answer;
import com.edmitryv.ml_market_server.marketplace.models.Review;
import com.edmitryv.ml_market_server.marketplace.repos.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AnswerService {
    @Autowired
    private AnswerRepository answerRepository;

    public Answer findByReview(Review review) {
        return answerRepository.findByReview(review);
    }
    public List<Answer> findAllByAuthor(DeveloperAccount author){
        return answerRepository.findAllByAuthor(author);
    }

    public Answer findById(Long id){
        return answerRepository.findById(id).orElse(null);
    }

    public Answer saveAnswer(Answer answer){
        return answerRepository.save(answer);
    }

    public void deleteAnswerById(Long id){
        answerRepository.deleteById(id);
    }
}
