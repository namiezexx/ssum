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

    public void checkUserPresent(String uid) {

        Optional<User> user = Optional.ofNullable(userJpaRepo.findByUid(uid));
        user.ifPresent(u -> {
            throw new CUserExistException();
        });
    }

    public void checkSocialUserPresent(String uid, String provider) {

        Optional<User> user = Optional.ofNullable(userJpaRepo.findByUidAndProvider(uid, provider));
        user.ifPresent(u -> {
            throw new CUserExistException();
        });
    }

    public User findUser(String uid) {
        return Optional.ofNullable(userJpaRepo.findByUid(uid)).orElseThrow(CUserNotFoundException::new);
    }

    public Page<User> findUsers(Pageable pageable) {
        return Optional.ofNullable(userJpaRepo.findAll(pageable)).orElseThrow(CUserNotFoundException::new);
    }

    public boolean insertUser(User user) {
        userJpaRepo.save(user);
        return true;
    }

    public User updateUser(String uid, String name) {

        User user = findUser(uid);
        user.setName(name);
        return user;
    }

    public boolean deleteUser(String uid) {

        User user = Optional.ofNullable(userJpaRepo.findByUid(uid)).orElseThrow(CUserNotFoundException::new);

        readingHistoryJpaRepo.deleteReadingHistoryByUser(user);
        postJpaRepo.deletePostByUser(user);
        userJpaRepo.deleteById(user.getMsrl());

        return true;
    }
}
