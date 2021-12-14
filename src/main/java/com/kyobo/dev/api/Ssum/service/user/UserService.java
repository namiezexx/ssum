package com.kyobo.dev.api.Ssum.service.user;

import com.kyobo.dev.api.Ssum.advice.exception.CResourceNotExistException;
import com.kyobo.dev.api.Ssum.advice.exception.CUserExistException;
import com.kyobo.dev.api.Ssum.advice.exception.CUserNotFoundException;
import com.kyobo.dev.api.Ssum.entity.Board;
import com.kyobo.dev.api.Ssum.entity.Post;
import com.kyobo.dev.api.Ssum.entity.User;
import com.kyobo.dev.api.Ssum.repository.PostJpaRepo;
import com.kyobo.dev.api.Ssum.repository.ReadingHistoryJpaRepo;
import com.kyobo.dev.api.Ssum.repository.UserJpaRepo;
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

    public void checkUserPresentByEmail(String email) {

        Optional<User> user = Optional.ofNullable(userJpaRepo.findByEmail(email));
        user.ifPresent(u -> {
            throw new CUserExistException();
        });
    }

    public void checkSocialUserPresentBySocialId(String socialId, String provider) {

        Optional<User> user = Optional.ofNullable(userJpaRepo.findByEmailAndProvider(socialId, provider));
        user.ifPresent(u -> {
            throw new CUserExistException();
        });
    }

    public User findUserByEmail(String email) {
        return Optional.ofNullable(userJpaRepo.findByEmail(email)).orElseThrow(CUserNotFoundException::new);
    }

    public Page<User> findUsers(Pageable pageable) {
        return Optional.ofNullable(userJpaRepo.findAll(pageable)).orElseThrow(CUserNotFoundException::new);
    }

    public User updateUser(User user) {
        return userJpaRepo.save(user);
    }

    public boolean deleteUser(User user) {
        readingHistoryJpaRepo.deleteReadingHistoryByUser(user);
        postJpaRepo.deletePostByUser(user);
        userJpaRepo.delete(user);

        return true;
    }
}
