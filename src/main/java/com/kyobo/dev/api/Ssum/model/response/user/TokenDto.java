package com.kyobo.dev.api.Ssum.model.response.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenDto {

    private String accessToken;
    private String refreshToken;
}
