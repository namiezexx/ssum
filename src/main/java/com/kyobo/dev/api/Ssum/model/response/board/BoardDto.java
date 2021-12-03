package com.kyobo.dev.api.Ssum.model.response.board;

import com.kyobo.dev.api.Ssum.entity.Board;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class BoardDto {

    private Long boardId;
    private String name;

}
