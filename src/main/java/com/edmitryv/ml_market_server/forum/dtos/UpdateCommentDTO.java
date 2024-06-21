package com.edmitryv.ml_market_server.forum.dtos;

import lombok.Data;

@Data
public class UpdateCommentDTO {
    private Long id;
    private String text;
}