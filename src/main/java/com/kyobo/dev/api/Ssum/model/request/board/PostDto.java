package com.kyobo.dev.api.Ssum.model.request.board;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
public class PostDto {

    @NotEmpty
    @Size(min=2, max=50)
    @ApiModelProperty(value = "작성자명", required = true)
    private String author;

    @NotEmpty
    @Size(min=2, max=100)
    @ApiModelProperty(value = "제목", required = true)
    private String title;

    @Size(min=2, max=500)
    @ApiModelProperty(value = "내용", required = true)
    private String content;

    @Size(min = 10, max = 1024)
    @ApiModelProperty(value = "썸네일", required = false)
    private String thumbnailUrl;

}