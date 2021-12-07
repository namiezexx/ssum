package com.kyobo.dev.api.Ssum.model.request.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value = "accssToken 갱신", description = "refreshToken을 가진 객체")
@Getter
@Setter
public class RefreshTokenDto {

    @ApiModelProperty(value = "refreshToken", example = "", required = true)
    private String refreshToken;
}
