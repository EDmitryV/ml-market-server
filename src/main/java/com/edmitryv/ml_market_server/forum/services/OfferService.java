package com.edmitryv.ml_market_server.forum.services;

import com.edmitryv.ml_market_server.core.models.CustomerAccount;
import com.edmitryv.ml_market_server.forum.models.Offer;
import com.edmitryv.ml_market_server.forum.models.OfferScore;
import com.edmitryv.ml_market_server.forum.models.Request;
import com.edmitryv.ml_market_server.forum.models.RequestScore;
import com.edmitryv.ml_market_server.forum.repos.OfferRepository;
import com.edmitryv.ml_market_server.forum.repos.OfferScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OfferService {
    @Autowired
    private OfferRepository offerRepository;
    @Autowired
    private OfferScoreRepository offerScoreRepository;
public Offer findById(Long id){
    return offerRepository.findById(id).orElse(null);
}
    public List<Offer> getAllOffers(){
        return offerRepository.findAll();
    }

    public Offer getOfferById(Long id){
        return offerRepository.findById(id).orElse(null);
    }

    public Offer saveOffer(Offer offer){
        return offerRepository.save(offer);
    }

    public void deleteOfferById(Long id){
        offerRepository.deleteById(id);
    }

    public OfferScore saveOfferScore(OfferScore offerScore){
        return offerScoreRepository.save(offerScore);
    }
    public void deleteOfferScoreById(Long id){offerScoreRepository.deleteById(id);}

    public OfferScore findScoreByOfferAndAuthor(Offer offer, CustomerAccount author){
        List<OfferScore> scoreList = offerScoreRepository.findByTargetOfferAndAuthor(offer, author);
        return scoreList.isEmpty()?null:scoreList.get(0);
    }
}
