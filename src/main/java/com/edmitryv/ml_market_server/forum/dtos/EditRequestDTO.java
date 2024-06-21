package com.edmitryv.ml_market_server.forum.dtos;

import com.edmitryv.ml_market_server.forum.models.Request;
import lombok.Data;

@Data
public class EditRequestDTO {
    private Long id;
    private String title;
    private String description;
    private Long taskId;


}
