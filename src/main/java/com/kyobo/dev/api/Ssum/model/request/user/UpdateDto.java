package com.kyobo.dev.api.Ssum.model.request.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ApiModel(value = "회원 정보 수정", description = "이름 가진 객체")
@Getter
@Setter
@ToString
public class UpdateDto {

    @ApiModelProperty(value = "이름", example = "김철수", required = true)
    private String name;

    @ApiModelProperty(value = "휴대폰번호", example = "01012345678", required = false)
    private String phone;

    @ApiModelProperty(value = "프로필이미지", example = "", required = false)
    private String profileImageUrl;
}
