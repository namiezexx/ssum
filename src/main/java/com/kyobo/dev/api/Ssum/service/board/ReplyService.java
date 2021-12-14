package com.kyobo.dev.api.Ssum.service.board;

import com.kyobo.dev.api.Ssum.entity.Post;
import com.kyobo.dev.api.Ssum.entity.Reply;
import com.kyobo.dev.api.Ssum.entity.User;
import com.kyobo.dev.api.Ssum.repository.ReplyJpaRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReplyService {

    private final BoardService boardService;

    private final ReplyJpaRepo replyJpaRepo;

    public Reply addReply(String contents, User user, Long postId) {

        Post post = boardService.findPost(postId);

        Reply reply = new Reply(contents, user, post);
        reply = replyJpaRepo.save(reply);

        return reply;
    }
}
