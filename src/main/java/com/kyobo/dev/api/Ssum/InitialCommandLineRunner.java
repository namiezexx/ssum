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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitialCommandLineRunner implements CommandLineRunner {

    private final UserJpaRepo userJpaRepo;
    private final BoardJpaRepo boardJpaRepo;
    private final PostJpaRepo postJpaRepo;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        final int index = 20;

        List<Board> list = new ArrayList<>(4);

        list.add(new Board("novel"));
        boardJpaRepo.save(list.get(0));

        list.add(new Board("essay"));
        boardJpaRepo.save(list.get(1));

        list.add(new Board("poem"));
        boardJpaRepo.save(list.get(2));

        list.add(new Board("free"));
        boardJpaRepo.save(list.get(3));

        for(int i=0; i<index; i++) {

            String uid = "test" + i + "@naver.com";
            String name = "홍길동" + i;
            String title = "title" + i;
            String contents = "예제 Contents" + i;

            User user = User.builder()
                    .uid(uid)
                    .password(passwordEncoder.encode("12345"))
                    .name(name)
                    .roles(Collections.singletonList("ROLE_USER"))
                    .build();
            userJpaRepo.save(user);

            Post post = new Post(user, list.get(i % list.size()), name, title, contents);
            postJpaRepo.save(post);
        }
    }
}
