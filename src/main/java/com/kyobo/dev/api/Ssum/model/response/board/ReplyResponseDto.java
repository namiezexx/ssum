package com.kyobo.dev.api.Ssum.model.response.board;

import com.kyobo.dev.api.Ssum.model.request.board.PostDto;
import com.kyobo.dev.api.Ssum.model.response.user.UserDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReplyResponseDto {

    private Long replyId;
    private String contents;
    private UserDto replyOwner;
    private PostResponseDto post;
}
