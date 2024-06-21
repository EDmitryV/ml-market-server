package com.edmitryv.ml_market_server.forum.services;

import com.edmitryv.ml_market_server.core.models.CustomerAccount;
import com.edmitryv.ml_market_server.forum.models.Comment;
import com.edmitryv.ml_market_server.forum.models.CommentScore;
import com.edmitryv.ml_market_server.forum.models.Offer;
import com.edmitryv.ml_market_server.forum.models.OfferScore;
import com.edmitryv.ml_market_server.forum.repos.CommentRepository;
import com.edmitryv.ml_market_server.forum.repos.CommentScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentScoreRepository commentScoreRepository;

    public Comment findById(Long id){
        return commentRepository.findById(id).orElse(null);
    }
    public List<Comment> getAllComments(){
        return commentRepository.findAll();
    }

    public Comment getCommentById(Long id){
        return commentRepository.findById(id).orElse(null);
    }

    public Comment saveComment(Comment comment){
        return commentRepository.save(comment);
    }

    public void deleteCommentById(Long id){
        commentRepository.deleteById(id);
    }

    public CommentScore saveCommentScore(CommentScore commentScore){
        return commentScoreRepository.save(commentScore);
    }
    public void deleteCommentScoreById(Long id){commentScoreRepository.deleteById(id);}

    public CommentScore findScoreByCommentAndAuthor(Comment comment, CustomerAccount author){
        List<CommentScore> scoreList = commentScoreRepository.findByTargetCommentAndAuthor(comment, author);
        return scoreList.isEmpty()?null:scoreList.get(0);
    }
}
