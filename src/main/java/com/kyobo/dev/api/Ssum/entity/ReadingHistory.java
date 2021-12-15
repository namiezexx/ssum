package com.kyobo.dev.api.Ssum.entity;

import com.kyobo.dev.api.Ssum.entity.common.CommonDateEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class ReadingHistory extends CommonDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long readingHistoryId;

    @Column(columnDefinition = "integer default 0")
    private Integer readingCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId")
    private Post post;

    public ReadingHistory(int readingCount, User user, Post post) {
        this.readingCount = readingCount;
        this.user = user;
        this.post = post;
    }

    public ReadingHistory addReadingCount() {
        this.readingCount++;
        return this;
    }
}
