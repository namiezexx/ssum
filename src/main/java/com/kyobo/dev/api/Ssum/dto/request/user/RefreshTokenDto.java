package com.kyobo.dev.api.Ssum.dto.request.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ApiModel(value = "accssToken 갱신", description = "refreshToken을 가진 객체")
@Getter
@Setter
@ToString
public class RefreshTokenDto {

    @ApiModelProperty(value = "refreshToken", example = "", required = true)
    private String refreshToken;
}
