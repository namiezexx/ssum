package com.kyobo.dev.api.Ssum.service.security;

import com.kyobo.dev.api.Ssum.advice.exception.CUserNotFoundException;
import com.kyobo.dev.api.Ssum.repository.UserJpaRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserJpaRepo userJpaRepo;

    public UserDetails loadUserByUsername(String userPk) {
        return userJpaRepo.findById(Long.valueOf(userPk)).orElseThrow(() -> new CUserNotFoundException("사용자 정보가 없습니다."));
    }
}