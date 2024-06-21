package com.edmitryv.ml_market_server.marketplace.dtos;

import com.edmitryv.ml_market_server.core.models.Image;
import com.edmitryv.ml_market_server.core.models.User;
import com.edmitryv.ml_market_server.marketplace.models.AppModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CreateAppModelDTO {
    private String name;
    private String description;
    private Long coverImageId;
    private List<Long> imagesIds;
    private Long taskId;
    private double price;
    private String currency;
    private String characteristics;
    private String url;
}
