package com.kyobo.dev.api.Ssum.model.request.comment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ApiModel(value = "대댓글 작성", description = "댓글에 대댓글을 작성한다.")
@Getter
@Setter
@ToString
public class NestedCommentRequestDto {

    @ApiModelProperty(value = "내용", example = "대댓글입니다.", required = true)
    private String contents;

}
