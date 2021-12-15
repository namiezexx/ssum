package com.kyobo.dev.api.Ssum.entity;

import com.kyobo.dev.api.Ssum.entity.common.CommonDateEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends CommonDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false, length = 100)
    private String contents;

    @Column(nullable = false, length = 100)
    private Long userId;

    @Column(nullable = false, length = 100)
    private String userEmail;

    @Column(nullable = false, length = 100)
    private String userName;

    @Column(columnDefinition = "integer default 0")
    private Integer nestedCommentCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId")
    private Post post;

    public Comment(String contents, User user, Post post) {
        this.contents = contents;
        this.userId = user.getUserId();
        this.userEmail = user.getEmail();
        this.userName = user.getName();
        this.nestedCommentCount = 0;
        this.post = post;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Comment addNestedCommentCount() {
        this.nestedCommentCount++;
        return this;
    }
}
