package com.kyobo.dev.api.Ssum.dto.response.board;

import com.kyobo.dev.api.Ssum.dto.response.user.UserDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PostResponseDto {

    private Long postId;
    private String author;
    private String title;
    private String content;
    private String thumbnailUrl;
    private Integer views;
    private Integer likes;
    private BoardDto board;
    private UserDto postOwner;

}
