package com.kyobo.dev.api.Ssum.service.board;

import com.kyobo.dev.api.Ssum.advice.exception.CNotOwnerException;
import com.kyobo.dev.api.Ssum.advice.exception.CResourceNotExistException;
import com.kyobo.dev.api.Ssum.entity.Board;
import com.kyobo.dev.api.Ssum.entity.Post;
import com.kyobo.dev.api.Ssum.entity.ReadingHistory;
import com.kyobo.dev.api.Ssum.entity.User;
import com.kyobo.dev.api.Ssum.dto.request.board.PostDto;
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
public class BoardService {

    private final BoardJpaRepo boardJpaRepo;
    private final PostJpaRepo postJpaRepo;
    private final ReadingHistoryJpaRepo readingHistoryJpaRepo;

    public Board findBoard(String boardName) {
        return boardJpaRepo.findByName(boardName)
                .orElseThrow(CResourceNotExistException::new);
    }

    public Post findPost(Long postId) {
        return postJpaRepo.findPostByQuery(postId)
                .orElseThrow(CResourceNotExistException::new);
    }

    // 게시판 이름으로 게시물 리스트 조회.
    public Page<Post> findPosts(String boardName, Pageable pageable) {
        Board board = findBoard(boardName);
        return postJpaRepo.findByBoard(board, pageable)
                .orElseThrow(CResourceNotExistException::new);
    }

    public Page<Post> findPosts(Pageable pageable) {
        return Optional.of(postJpaRepo.findAll(pageable))
                .orElseThrow(CResourceNotExistException::new);
    }

    // 게시물을 등록합니다. 게시물의 회원UID가 조회되지 않으면 CUserNotFoundException 처리합니다.
    public Post writePost(User user, String boardName, PostDto postDto) {

        Board board = findBoard(boardName);
        return postJpaRepo.save(new Post(user, board, postDto.getAuthor(), postDto.getTitle(), postDto.getContent(), postDto.getThumbnailUrl()));
    }

    // 게시물을 수정합니다. 게시물 등록자와 로그인 회원정보가 틀리면 CNotOwnerException 처리합니다.
    public Post updatePost(User user, long postId, PostDto postDto) {

        Post post = findPost(postId);

        if (!user.getEmail().equals(post.getPostOwner().getEmail()))
            throw new CNotOwnerException();

        // 영속성 컨텍스트의 변경감지(dirty checking) 기능에 의해 조회한 Post내용을 변경만 해도 Update쿼리가 실행됩니다.
        return post.setUpdate(postDto.getAuthor(), postDto.getTitle(), postDto.getContent());
    }

    // 게시물을 삭제합니다. 게시물 등록자와 로그인 회원정보가 틀리면 CNotOwnerException 처리합니다.
    public boolean deletePost(User user, long postId) {

        Post post = findPost(postId);

        if (!user.getEmail().equals(post.getPostOwner().getEmail()))
            throw new CNotOwnerException();

        postJpaRepo.delete(post);

        return true;
    }

    public Post updateReadingHistory(User user, long postId) {

        Post post = findPost(postId);

        ReadingHistory readingHistory = readingHistoryJpaRepo.selectReadingHistoryByUserAndPost(user, post)
                .orElseGet(() -> new ReadingHistory(0, user, post));

        post.addViews();

        readingHistory.addReadingCount();
        readingHistoryJpaRepo.save(readingHistory);

        return post;
    }
}