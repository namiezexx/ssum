package com.kyobo.dev.api.Ssum.dto.response.comment;

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
