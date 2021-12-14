package com.kyobo.dev.api.Ssum.entity;

import com.kyobo.dev.api.Ssum.entity.common.CommonDateEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Reply extends CommonDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyId;

    @Column(nullable = false, length = 100)
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User replyOwner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId")
    private Post post;

    public Reply(String contents, User replyOwner, Post post) {
        this.contents = contents;
        this.replyOwner = replyOwner;
        this.post = post;
    }
}
