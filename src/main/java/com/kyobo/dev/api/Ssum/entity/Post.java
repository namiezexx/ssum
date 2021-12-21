package com.kyobo.dev.api.Ssum.entity;

import com.kyobo.dev.api.Ssum.dto.request.board.PostDto;
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

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<ReadingHistory> readingHistories;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> comments;

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

    public Post(User postOwner, Board board, PostDto postDto) {
        this.postOwner = postOwner;
        this.board = board;
        //this.author = postDto.getAuthor();
        this.author = postOwner.getName();  // 글 작성자는 로그인한 유저로 저장
        this.title = postDto.getTitle();
        this.content = postDto.getContent();
        this.thumbnailUrl = postDto.getThumbnailUrl();
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

    public Post setUpdate(PostDto postDto) {
        //this.author = postDto.getAuthor(); // 글 작성자는 최초 작성한 유저로 저장. 수정 불가.
        this.title = postDto.getTitle();
        this.content = postDto.getContent();
        return this;
    }

    public Post addViews() {
        this.views++;
        return this;
    }
}