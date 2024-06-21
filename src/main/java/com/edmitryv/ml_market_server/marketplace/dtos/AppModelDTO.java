package com.edmitryv.ml_market_server.marketplace.dtos;

import com.edmitryv.ml_market_server.core.models.Image;
import com.edmitryv.ml_market_server.core.models.User;
import com.edmitryv.ml_market_server.forum.dtos.OfferDTO;
import com.edmitryv.ml_market_server.forum.models.Offer;
import com.edmitryv.ml_market_server.marketplace.models.AppModel;
import com.edmitryv.ml_market_server.marketplace.models.Review;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AppModelDTO {
    private Long id;
    private String name;
    private String description;
    private Long coverImageId;
    private List<Long> imagesIds;
    private Long taskId;
    private String taskName;
    private int downloads;
    private Long authorId;
    private String authorUsername;
    private Long authorProfileImageId;
    private double price;
    private String currency;
    private List<ReviewDTO> reviews;
    private double scores;
    private List<OfferDTO> offers;
    private boolean purchased;
    private String characteristics;
    private String url;

    public AppModelDTO(AppModel appModel, User requesting) {
        this.id = appModel.getId();
        this.name = appModel.getName();
        this.description = appModel.getDescription();
        if(appModel.getCoverImage()!=null)
            this.coverImageId = appModel.getCoverImage().getId();
        else
            this.coverImageId = -1L;
        this.imagesIds = new ArrayList<>();
        for (Image image : appModel.getImages().stream().toList()) {
            this.imagesIds.add(image.getId());
        }
        this.taskId = appModel.getTask().getId();
        this.taskName = appModel.getTask().getName();
        this.downloads = appModel.getDownloads();
        this.authorId = appModel.getAuthor().getUser().getId();
        this.authorUsername = appModel.getAuthor().getUser().getUsername();
        if(appModel.getAuthor().getUser().getProfileImage()!=null)
            this.authorProfileImageId = appModel.getAuthor().getUser().getProfileImage().getId();
        else
            this.authorProfileImageId = -1L;
        this.price = appModel.getPrice();
        this.currency = appModel.getCurrency();
        this.reviews = new ArrayList<>();
        for (Review review : appModel.getReviews()) {
            this.reviews.add(new ReviewDTO(review));
        }
        this.scores = appModel.getScores();
        this.offers = new ArrayList<>();
        for (Offer offer : appModel.getOffers()) {
            this.offers.add(new OfferDTO(offer, requesting));
        }
        if (requesting != null) {
            this.purchased = appModel.getOwners().contains(requesting.getCustomerAccount());
        } else {
            this.purchased = false;
        }
        this.characteristics = appModel.getCharacteristics();
        this.url = appModel.getUrl();
    }
}
