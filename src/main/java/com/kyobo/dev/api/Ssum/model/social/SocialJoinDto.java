package com.kyobo.dev.api.Ssum.model.social;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ApiModel(value = "소셜 로그인 정보", description = "Kakao accessToken을 가진 객체")
@Getter
@Setter
@ToString
public class SocialJoinDto {

    @ApiModelProperty(value = "Kakao accessToken", example = "", required = true)
    private String accessToken;

    @ApiModelProperty(value = "이름", example = "홍길동", required = true)
    private String name;

    @ApiModelProperty(value = "휴대폰번호", example = "01012345678", required = false)
    private String phone;

    @ApiModelProperty(value = "프로필이미지", example = "", required = false)
    private String profileImageUrl;
}
