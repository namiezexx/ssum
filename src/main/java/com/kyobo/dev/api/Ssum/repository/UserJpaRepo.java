package com.kyobo.dev.api.Ssum.repository;

import com.kyobo.dev.api.Ssum.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepo extends JpaRepository<User, Long> {

    Optional<User> findByUid(String email);
    Optional<User> findByUidAndProvider(String uid, String provider);

}
