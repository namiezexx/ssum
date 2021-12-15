package com.kyobo.dev.api.Ssum.model.response.board;

import com.kyobo.dev.api.Ssum.model.request.board.PostDto;
import com.kyobo.dev.api.Ssum.model.response.user.UserDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class CommentResponseDto {

    private Long commentId;
    private String contents;
    private Long userId;
    private String userEmail;
    private String userName;
    private Integer nestedCommentCount;
    private LocalDateTime modifiedAt;
}
