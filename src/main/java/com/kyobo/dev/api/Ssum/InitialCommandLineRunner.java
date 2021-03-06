package com.kyobo.dev.api.Ssum;

import com.kyobo.dev.api.Ssum.entity.Board;
import com.kyobo.dev.api.Ssum.entity.Post;
import com.kyobo.dev.api.Ssum.entity.User;
import com.kyobo.dev.api.Ssum.repository.BoardJpaRepo;
import com.kyobo.dev.api.Ssum.repository.PostJpaRepo;
import com.kyobo.dev.api.Ssum.repository.UserJpaRepo;
import com.kyobo.dev.api.Ssum.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitialCommandLineRunner implements CommandLineRunner {

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;

    private final UserJpaRepo userJpaRepo;
    private final BoardJpaRepo boardJpaRepo;
    private final PostJpaRepo postJpaRepo;

    private final BoardService boardService;

    private final PasswordEncoder passwordEncoder;

    public String getDdlAuto() {
        return ddlAuto;
    }

    public void setDdlAuto(String ddlAuto) {
        this.ddlAuto = ddlAuto;
    }

    @Override
    public void run(String... args) throws Exception {

        if(ddlAuto.equals("create")){
            initData();
        }
    }

    public void initData() {

        final int index = 100;

        String profileImageUrl = "https://namiezexx-test-bucket.s3.ap-northeast-2.amazonaws.com/ssum/mylib/author/author";
        String thumbnailUrl = "https://namiezexx-test-bucket.s3.ap-northeast-2.amazonaws.com/ssum/mylib/book/mini-book";

        List<Board> list = new ArrayList<>(4);

        list.add(new Board("novel"));
        boardJpaRepo.save(list.get(0));

        list.add(new Board("essay"));
        boardJpaRepo.save(list.get(1));

        list.add(new Board("poem"));
        boardJpaRepo.save(list.get(2));

        list.add(new Board("free"));
        boardJpaRepo.save(list.get(3));

        for(int i=1; i<=index; i++) {

            String email = "test" + i + "@naver.com";
            String name = "?????????" + i;
            String title = "title" + i;
            String contents = "?????? Contents" + i;

            User user = User.builder()
                    .email(email)
                    .password(passwordEncoder.encode("12345"))
                    .name(name)
                    .phone("01012341234")
                    .profileImageUrl(profileImageUrl + ((i % 4) + 1) + ".png")
                    .roles(Collections.singletonList("ROLE_USER"))
                    .build();
            userJpaRepo.save(user);

            Post post = new Post(user, list.get(i % list.size()), name, title, contents, thumbnailUrl + ((i % 10)+1) + ".png");
            postJpaRepo.save(post);
        }

        /*Random random = new Random();
        for(int i=1; i<=index; i++) {
            String email = "test" + i + "@naver.com";
            boardService.updateReadingHistory(email, random.nextInt(100)+1);
        }*/
    }
}
