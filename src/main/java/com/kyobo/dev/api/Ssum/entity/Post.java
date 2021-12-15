package com.kyobo.dev.api.Ssum.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kyobo.dev.api.Ssum.entity.common.CommonDateEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Post extends CommonDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(nullable = false, length = 50)
    private String author;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    private String content;

    @Column(nullable = true, length = 1024)
    private String thumbnailUrl;

    @Column(columnDefinition = "integer default 0")
    private Integer views;

    @Column(columnDefinition = "integer default 0")
    private Integer likes;

    //@JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boardId")
    private Board board; // 게시글 - 게시판의 관계 - N:1

    //@JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User postOwner;  // 게시글 - 회원의 관계 - N:1

    //@OneToMany(mappedBy = "readingHistoryId")
    //private List<ReadingHistory> readingHistories;

    // 생성자
    public Post(User postOwner, Board board, String author, String title, String content, String thumbnailUrl) {
        this.postOwner = postOwner;
        this.board = board;
        this.author = author;
        this.title = title;
        this.content = content;
        this.thumbnailUrl = thumbnailUrl;
        this.views = 0;
        this.likes = 0;
    }

    // 수정시 데이터 처리
    public Post setUpdate(String author, String title, String content) {
        this.author = author;
        this.title = title;
        this.content = content;
        return this;
    }

    public Post addViews() {
        this.views++;
        return this;
    }
}