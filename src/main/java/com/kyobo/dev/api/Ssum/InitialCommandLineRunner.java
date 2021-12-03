package com.kyobo.dev.api.Ssum;

import com.kyobo.dev.api.Ssum.entity.Board;
import com.kyobo.dev.api.Ssum.entity.Post;
import com.kyobo.dev.api.Ssum.entity.User;
import com.kyobo.dev.api.Ssum.repository.BoardJpaRepo;
import com.kyobo.dev.api.Ssum.repository.PostJpaRepo;
import com.kyobo.dev.api.Ssum.repository.UserJpaRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class InitialCommandLineRunner implements CommandLineRunner {

    private final UserJpaRepo userJpaRepo;
    private final BoardJpaRepo boardJpaRepo;
    private final PostJpaRepo postJpaRepo;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        /**
         * CommandLineRunner의 run 메소드를 Override하여 초기 데이터 세팅
         * 스프링 컨텍스트 구성이 완료되면 CommendLineRunner의 메소드가 호출된다.
         */
        setData("jaesuklee@naver.com", "12345", "이재석", "novel", "듄 시리즈 1권", "전세계적으로 가장 많은 독자를 지닌 SF 문학사의 기념비");
        setData("woojinlee@naver.com", "12345", "이우진", "essay", "다정하기 싫어서 다정하게", "다정할 수만 없는 세상을 쓰다듬는 다정한 마음");
        setData("ahreumkim@naver.com", "12345", "김아름", "poem", "강승한 동시선집", "눈물지어요");
        setData("woochanlee@naver.com", "12345", "이우찬", "free", "곱셈구구", "1~9단 배우기");
    }

    public void setData(String uid, String password, String name, String boardName, String title, String contents) {

        User user = User.builder()
                .uid(uid)
                .password(passwordEncoder.encode(password))
                .name(name)
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
        userJpaRepo.save(user);

        Board board = new Board(boardName);
        boardJpaRepo.save(board);

        Post post = new Post(user, board, name, title, contents);
        postJpaRepo.save(post);
    }

}
