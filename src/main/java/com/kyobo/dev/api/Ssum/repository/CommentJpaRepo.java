package com.kyobo.dev.api.Ssum.repository;

import com.kyobo.dev.api.Ssum.entity.Comment;
import com.kyobo.dev.api.Ssum.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CommentJpaRepo extends JpaRepository<Comment, Long> {

    Optional<Page<Comment>> findByPost(Post post, Pageable pageable);

    @Transactional
    @Modifying
    @Query("delete from Comment c where c.userId = :userId")
    void deleteCommentByUserId(@Param("userId") Long userId);
}
