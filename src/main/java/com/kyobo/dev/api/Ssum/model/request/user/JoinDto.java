package com.kyobo.dev.api.Ssum.model.request.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ApiModel(value = "가입 정보", description = "아이디, 패스워드, 이름을 가진 객체")
@Getter
@Setter
@ToString
public class JoinDto {

    @ApiModelProperty(value = "아이디", example = "test@naver.com", required = true)
    private String email;

    @ApiModelProperty(value = "패스워드", example = "12345", required = true)
    private String password;

    @ApiModelProperty(value = "이름", example = "홍길동", required = true)
    private String name;

    @ApiModelProperty(value = "휴대폰번호", example = "01012345678", required = false)
    private String phone;

    @ApiModelProperty(value = "프로필이미지", example = "", required = false)
    private String profileImageUrl;
}
