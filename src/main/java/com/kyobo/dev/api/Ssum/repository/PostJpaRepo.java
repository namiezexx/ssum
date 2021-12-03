package com.kyobo.dev.api.Ssum.repository;

import com.kyobo.dev.api.Ssum.entity.Board;
import com.kyobo.dev.api.Ssum.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostJpaRepo extends JpaRepository<Post, Long> {
    List<Post> findByBoard(Board board);
}