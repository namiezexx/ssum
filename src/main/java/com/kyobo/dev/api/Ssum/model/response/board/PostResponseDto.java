package com.kyobo.dev.api.Ssum.model.response.board;

import com.kyobo.dev.api.Ssum.model.response.user.UserDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostResponseDto {

    private Long postId;
    private String author;
    private String title;
    private String content;
    private int views;
    private int likes;
    private BoardDto board;
    private UserDto user;

}
