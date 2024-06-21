package com.edmitryv.ml_market_server.forum.dtos;

import com.edmitryv.ml_market_server.forum.models.Offer;
import lombok.Data;

@Data
public class CreateOfferDTO {
    private String remark;
    private Long appId;
    private Long targetRequestId;
}
