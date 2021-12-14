package com.kyobo.dev.api.Ssum.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kyobo.dev.api.Ssum.entity.common.CommonDateEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Board extends CommonDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @Column(nullable = false, length = 100)
    private String name;

    //@OneToMany(mappedBy = "postId")
    //private List<Post> posts;

    public Board(String name) {
        this.name = name;
    }
}