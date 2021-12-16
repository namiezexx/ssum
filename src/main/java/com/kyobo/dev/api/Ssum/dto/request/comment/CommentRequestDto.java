package com.kyobo.dev.api.Ssum.dto.request.comment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ApiModel(value = "댓글 작성", description = "게시글에 댓글을 작성한다.")
@Getter
@Setter
@ToString
public class CommentRequestDto {

    @ApiModelProperty(value = "내용", example = "댓글입니다.", required = true)
    private String contents;

}
