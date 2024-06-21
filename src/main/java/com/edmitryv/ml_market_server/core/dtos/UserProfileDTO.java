package com.edmitryv.ml_market_server.core.dtos;

import com.edmitryv.ml_market_server.core.models.User;
import lombok.Data;

@Data
public class UserProfileDTO {

    private long id;
    private long profileImageId;
    private String email;
    private String username;
    private long developerAccountId;
    private int developedAppsNum;
    private int offersNum;
    private int answersNum;
    private long customerAccountId;
    private int purchasedAppsNum;
    private int reviewsNum;
    private int createdRequestsNum;


    public UserProfileDTO(User user){
        if(user != null) {
            id = user.getId();
            username = user.getUsername();
            if(user.getProfileImage()!=null)
            profileImageId = user.getProfileImage().getId();
            else profileImageId = -1;
            email = user.getEmail();
            developerAccountId= user.getDeveloperAccount().getId();
            developedAppsNum=user.getDeveloperAccount().getDevelopedAppModels().size();
            offersNum=user.getDeveloperAccount().getOffers().size();
            answersNum=user.getDeveloperAccount().getReviewsAnswers().size();
            customerAccountId=user.getCustomerAccount().getId();
            purchasedAppsNum=user.getCustomerAccount().getPurchasedAppModels().size();
            reviewsNum=user.getCustomerAccount().getReviews().size();
            createdRequestsNum=user.getCustomerAccount().getRequests().size();
        }
    }
}
