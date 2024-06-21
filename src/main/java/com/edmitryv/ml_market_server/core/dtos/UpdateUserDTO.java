package com.edmitryv.ml_market_server.core.dtos;

import com.edmitryv.ml_market_server.core.models.User;
import lombok.Data;


@Data
public class UpdateUserDTO {

    private long id;
    private long profileImageId;
    private String email;
    private String username;
    private String password;
}
