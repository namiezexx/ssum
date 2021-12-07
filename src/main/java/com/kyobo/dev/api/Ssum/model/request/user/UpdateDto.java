package com.kyobo.dev.api.Ssum.model.request.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value = "회원 정보 수정", description = "이름 가진 객체")
@Getter
@Setter
public class UpdateDto {

    @ApiModelProperty(value = "이름", example = "김철수", required = true)
    private String name;
}
