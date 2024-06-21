package com.edmitryv.ml_market_server.marketplace.dtos;

import lombok.Data;

@Data
public class CreateReviewDTO {
    private int score;
    private String text;
    private Long appId;
}
