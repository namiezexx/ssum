package com.kyobo.dev.api.Ssum.model.request.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value = "로그인 정보", description = "아이디, 패스워드를 가진 객체")
@Getter
@Setter
public class LoginDto {

    @ApiModelProperty(value = "아이디", example = "test@naver.com", required = true)
    private String id;

    @ApiModelProperty(value = "패스워드", example = "12345", required = true)
    private String password;
}
