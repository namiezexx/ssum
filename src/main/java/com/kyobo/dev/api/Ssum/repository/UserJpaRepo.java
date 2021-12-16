package com.kyobo.dev.api.Ssum.repository;

import com.kyobo.dev.api.Ssum.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepo extends JpaRepository<User, Long> {

    Page<User> findAll(Pageable pageable);

    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndProvider(String uid, String provider);

}
