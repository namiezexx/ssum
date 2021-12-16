package com.kyobo.dev.api.Ssum.service.user;

import com.kyobo.dev.api.Ssum.advice.exception.CUserExistException;
import com.kyobo.dev.api.Ssum.advice.exception.CUserNotFoundException;
import com.kyobo.dev.api.Ssum.entity.User;
import com.kyobo.dev.api.Ssum.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserJpaRepo userJpaRepo;
    private final PostJpaRepo postJpaRepo;
    private final ReadingHistoryJpaRepo readingHistoryJpaRepo;
    private final CommentJpaRepo commentJpaRepo;
    private final NestedCommentJpaRepo nestedCommentJpaRepo;

    public void checkUserPresentByEmail(String email) {

        // 회원가입 시 입력한 email 로 기등록된 사용자가 있다면 예외처리
        userJpaRepo.findByEmail(email)
                .ifPresent(user -> {
                    throw new CUserExistException();
                });
    }

    public void checkSocialUserPresentBySocialId(String socialId, String provider) {

        userJpaRepo.findByEmailAndProvider(socialId, provider)
                .ifPresent(user -> {
            throw new CUserExistException();
        });
    }

    public User findUserByEmail(String email) {
        return userJpaRepo.findByEmail(email)
                .orElseThrow(CUserNotFoundException::new);
    }

    public User findByEmailAndProvider(String id, String provider) {
        return userJpaRepo.findByEmailAndProvider(id, provider)
                .orElseThrow(CUserNotFoundException::new);
    }

    public Page<User> findUsers(Pageable pageable) {
        return userJpaRepo.findUsers(pageable)
                .orElseThrow(CUserNotFoundException::new);
    }

    public User updateUser(User user) {
        return userJpaRepo.save(user);
    }

    public boolean deleteUser(User user) {

        // user 의 경우 JWT를 통해 이미 조회 후 생성한 인스턴스이므로 별도 체크 없이 삭제한다.
        userJpaRepo.delete(user);

        return true;
    }
}
