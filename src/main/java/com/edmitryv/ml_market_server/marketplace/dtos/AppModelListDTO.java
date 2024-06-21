package com.edmitryv.ml_market_server.marketplace.dtos;

import com.edmitryv.ml_market_server.marketplace.models.AppModel;
import lombok.Data;

@Data
public class AppModelListDTO {
    private Long id;
    private String name;
    private Long coverImageId;
    private int downloads;
    private String authorUsername;
    private Long authorProfileImageId;
    private double price;
    private String currency;
    private double scores;
    private String characteristics;
    public AppModelListDTO(AppModel appModel){
        this.id = appModel.getId();
        this.name = appModel.getName();
        if(appModel.getCoverImage()!=null)
            this.coverImageId = appModel.getCoverImage().getId();
        else
            this.coverImageId = -1L;
        this.downloads = appModel.getDownloads();
        this.authorUsername = appModel.getAuthor().getUser().getUsername();
        if(appModel.getAuthor().getUser().getProfileImage()!=null)
            this.authorProfileImageId = appModel.getAuthor().getUser().getProfileImage().getId();
        else
            this.authorProfileImageId = -1L;
        this.price = appModel.getPrice();
        this.currency = appModel.getCurrency();
        this.scores = appModel.getScores();
        this.characteristics = appModel.getCharacteristics();
    }
}
