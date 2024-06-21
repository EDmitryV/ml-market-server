package com.edmitryv.ml_market_server.forum.repos;

import com.edmitryv.ml_market_server.forum.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
