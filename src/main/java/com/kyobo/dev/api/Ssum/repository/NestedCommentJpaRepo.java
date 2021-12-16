package com.kyobo.dev.api.Ssum.repository;

import com.kyobo.dev.api.Ssum.entity.Comment;
import com.kyobo.dev.api.Ssum.entity.NestedComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface NestedCommentJpaRepo extends JpaRepository<NestedComment, Long> {

    Optional<Page<NestedComment>> findByComment(Comment comment, Pageable pageable);

    @Transactional
    @Modifying
    @Query("delete from NestedComment n where n.userId = :userId")
    void deleteNestedCommentByUserId(@Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query("delete from NestedComment n where n.comment = :comment")
    void deleteNestedCommentByComment(@Param("comment") Comment comment);
}
