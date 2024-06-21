package com.edmitryv.ml_market_server.forum.repos;

import com.edmitryv.ml_market_server.core.models.CustomerAccount;
import com.edmitryv.ml_market_server.forum.models.Comment;
import com.edmitryv.ml_market_server.forum.models.CommentScore;
import com.edmitryv.ml_market_server.forum.models.Offer;
import com.edmitryv.ml_market_server.forum.models.RequestScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentScoreRepository extends JpaRepository<CommentScore, Long> {
    List<CommentScore> findByTargetCommentAndAuthor(Comment targetComment, CustomerAccount author);
}
