package com.kyobo.dev.api.Ssum.model.response.comment;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class NestedCommentResponseDto {

    private Long nestedCommentId;
    private String contents;
    private Long userId;
    private String userEmail;
    private String userName;
    private LocalDateTime modifiedAt;
}
