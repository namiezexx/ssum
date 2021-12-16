package com.kyobo.dev.api.Ssum.service.comment;

import com.kyobo.dev.api.Ssum.advice.exception.CNotOwnerException;
import com.kyobo.dev.api.Ssum.advice.exception.CResourceNotExistException;
import com.kyobo.dev.api.Ssum.entity.Post;
import com.kyobo.dev.api.Ssum.entity.Comment;
import com.kyobo.dev.api.Ssum.entity.User;
import com.kyobo.dev.api.Ssum.dto.request.comment.CommentRequestDto;
import com.kyobo.dev.api.Ssum.repository.CommentJpaRepo;
import com.kyobo.dev.api.Ssum.repository.NestedCommentJpaRepo;
import com.kyobo.dev.api.Ssum.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final BoardService boardService;

    private final CommentJpaRepo commentJpaRepo;
    private final NestedCommentJpaRepo nestedCommentJpaRepo;

    public Comment addComment(String contents, User user, Long postId) {

        Post post = boardService.findPost(postId);

        Comment comment = new Comment(contents, user, post);

        return commentJpaRepo.save(comment);
    }

    public Page<Comment> findComments(long postId, Pageable pageable) {

        Post post = boardService.findPost(postId);
        return commentJpaRepo.findByPost(post, pageable)
                .orElseThrow(CResourceNotExistException::new);
    }

    public Comment updateComment(User user, long commentId, CommentRequestDto commentRequestDto) {

        Comment comment = commentJpaRepo.findById(commentId).orElseThrow(CResourceNotExistException::new);

        // 수정하는 댓글이 본인의 댓글이 아니면 에러처리.
        if(!comment.getUserId().equals(user.getUserId())) {
            throw new CNotOwnerException();
        }

        // 영속성 컨텍스트의 dirty checking 으로 update 가 자동으로 수행된다.
        comment.setContents(commentRequestDto.getContents());

        return comment;
    }

    public void deleteComment(User user, long commentId) {

        Comment comment = commentJpaRepo.findById(commentId).orElseThrow(CResourceNotExistException::new);

        // 삭제하는 댓글이 본인의 댓글이 아니면 에러처리.
        if(!comment.getUserId().equals(user.getUserId())) {
            throw new CNotOwnerException();
        }

        commentJpaRepo.delete(comment);
    }
}
