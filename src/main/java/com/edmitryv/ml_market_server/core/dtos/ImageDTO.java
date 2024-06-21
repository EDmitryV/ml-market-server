package com.edmitryv.ml_market_server.core.dtos;

import com.edmitryv.ml_market_server.core.models.Image;
import lombok.Data;

@Data
public class ImageDTO {
    private Long id;
    private Long ownerId;

    public ImageDTO(Image image){
        this.id = image.getId();
        this.ownerId = image.getOwner().getId();
    }
}
