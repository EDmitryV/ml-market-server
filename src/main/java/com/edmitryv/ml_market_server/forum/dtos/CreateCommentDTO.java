package com.edmitryv.ml_market_server.forum.dtos;

import lombok.Data;

@Data
public class CreateCommentDTO {

    private String text;
    private Long targetRequestId;
    private Long targetCommentId;
    private Long targetOfferId;
}
