package com.edmitryv.ml_market_server.authentication.models;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role{
    USER,
    ADMIN,
    MODERATOR_MARKETPLACE,
    MODERATOR_FORUM;
}

