package com.kyobo.dev.api.Ssum.service.comment;

import com.kyobo.dev.api.Ssum.advice.exception.CNotOwnerException;
import com.kyobo.dev.api.Ssum.advice.exception.CResourceNotExistException;
import com.kyobo.dev.api.Ssum.entity.Comment;
import com.kyobo.dev.api.Ssum.entity.NestedComment;
import com.kyobo.dev.api.Ssum.entity.User;
import com.kyobo.dev.api.Ssum.dto.request.comment.NestedCommentRequestDto;
import com.kyobo.dev.api.Ssum.repository.CommentJpaRepo;
import com.kyobo.dev.api.Ssum.repository.NestedCommentJpaRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class NestedCommentService {

    private final CommentJpaRepo commentJpaRepo;
    private final NestedCommentJpaRepo nestedCommentJpaRepo;

    public NestedComment addNestedComment(String contents, User user, Long commentId) {

        Comment comment = commentJpaRepo.findById(commentId).orElseThrow(CResourceNotExistException::new);

        NestedComment nestedComment = new NestedComment(contents, user, comment);

        comment.addNestedCommentCount();
        
        return nestedCommentJpaRepo.save(nestedComment);
    }

    public Page<NestedComment> findNestedComments(Long commentId, Pageable pageable) {

        Comment comment = commentJpaRepo.findById(commentId).orElseThrow(CResourceNotExistException::new);

        return nestedCommentJpaRepo.findByComment(comment, pageable)
                .orElseThrow(CResourceNotExistException::new);
    }

    public NestedComment updateNestedComment(User user, Long nestedCommentId, NestedCommentRequestDto commentRequestDto) {

        NestedComment nestedComment = nestedCommentJpaRepo.findById(nestedCommentId).orElseThrow(CResourceNotExistException::new);

        // 수정하는 댓글이 본인의 댓글이 아니면 에러처리.
        if(!nestedComment.getUserId().equals(user.getUserId())) {
            throw new CNotOwnerException();
        }

        // 영속성 컨텍스트의 dirty checking 으로 update 가 자동으로 수행된다.
        nestedComment.setContents(commentRequestDto.getContents());

        return nestedComment;
    }

    public void deleteNestedComment(User user, Long nestedCommentId) {

        NestedComment nestedComment = nestedCommentJpaRepo.findById(nestedCommentId).orElseThrow(CResourceNotExistException::new);

        // 삭제하는 댓글이 본인의 댓글이 아니면 에러처리.
        if(!nestedComment.getUserId().equals(user.getUserId())) {
            throw new CNotOwnerException();
        }

        nestedCommentJpaRepo.delete(nestedComment);
    }
}
