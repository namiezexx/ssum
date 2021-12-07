package com.kyobo.dev.api.Ssum.model.social;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value = "소셜 로그인 정보", description = "Kakao accessToken을 가진 객체")
@Getter
@Setter
public class SocialJoinDto {

    @ApiModelProperty(value = "Kakao accessToken", example = "", required = true)
    private String accessToken;

    @ApiModelProperty(value = "이름", example = "홍길동", required = true)
    private String name;
}