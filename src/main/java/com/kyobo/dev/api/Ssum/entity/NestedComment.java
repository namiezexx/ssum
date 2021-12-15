package com.kyobo.dev.api.Ssum.entity;

import com.kyobo.dev.api.Ssum.entity.common.CommonDateEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class NestedComment extends CommonDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nestedCommentId;

    @Column(nullable = false, length = 100)
    private String contents;

    @Column(nullable = false, length = 100)
    private Long userId;

    @Column(nullable = false, length = 100)
    private String userEmail;

    @Column(nullable = false, length = 100)
    private String userName;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "commentId")
    private Comment comment;

    public NestedComment(String contents, User user, Comment comment) {
        this.contents = contents;
        this.userId = user.getUserId();
        this.userEmail = user.getEmail();
        this.userName = user.getName();
        this.comment = comment;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

}
