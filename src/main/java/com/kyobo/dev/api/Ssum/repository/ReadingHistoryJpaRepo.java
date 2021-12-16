package com.kyobo.dev.api.Ssum.repository;

import com.kyobo.dev.api.Ssum.entity.Post;
import com.kyobo.dev.api.Ssum.entity.ReadingHistory;
import com.kyobo.dev.api.Ssum.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReadingHistoryJpaRepo extends JpaRepository<ReadingHistory, Long> {

    Page<ReadingHistory> findAll(Pageable pageable);

    @Query("select r from ReadingHistory r where r.user = :user and r.post = :post")
    Optional<ReadingHistory> selectReadingHistoryByUserAndPost(@Param("user")User user, @Param("post")Post post);

    @Modifying
    @Query("delete from ReadingHistory r where r.user = :user")
    void deleteReadingHistoryByUser(@Param("user")User user);

    @Modifying
    @Query("delete from ReadingHistory r where r.post = :post")
    void deleteReadingHistoryByPost(@Param("post") Post post);
}
