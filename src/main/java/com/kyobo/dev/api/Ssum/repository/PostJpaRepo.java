package com.kyobo.dev.api.Ssum.repository;

import com.kyobo.dev.api.Ssum.entity.Board;
import com.kyobo.dev.api.Ssum.entity.Post;
import com.kyobo.dev.api.Ssum.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PostJpaRepo extends JpaRepository<Post, Long> {
    Page<Post> findByBoard(Board board, Pageable pageable);
    Page<Post> findAll(Pageable pageable);

    @Query("select distinct p from Post p join fetch p.board b join fetch p.user where p.postId = :postId")
    Post findPostByQuery(@Param("postId") Long postId);

    @Transactional
    @Modifying
    @Query("delete from Post p where p.user = :user")
    void deletePostByUser(@Param("user") User user);
}
