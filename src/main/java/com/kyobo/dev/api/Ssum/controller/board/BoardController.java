package com.kyobo.dev.api.Ssum.controller.board;

import com.kyobo.dev.api.Ssum.entity.Board;
import com.kyobo.dev.api.Ssum.entity.Post;
import com.kyobo.dev.api.Ssum.model.response.board.BoardDto;
import com.kyobo.dev.api.Ssum.model.request.board.PostDto;
import com.kyobo.dev.api.Ssum.model.response.CommonResult;
import com.kyobo.dev.api.Ssum.model.response.ListResult;
import com.kyobo.dev.api.Ssum.model.response.SingleResult;
import com.kyobo.dev.api.Ssum.model.response.board.PostResponseDto;
import com.kyobo.dev.api.Ssum.service.ResponseService;
import com.kyobo.dev.api.Ssum.service.board.BoardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = {"3. Board"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/board")
public class BoardController {

    private final BoardService boardService;
    private final ResponseService responseService;

    private final ModelMapper modelMapper;

    @ApiOperation(value = "게시판 정보 조회", notes = "게시판 정보를 조회한다.")
    @GetMapping(value = "/{boardName}")
    public SingleResult<BoardDto> boardInfo(@PathVariable String boardName) {

        Board board = boardService.findBoard(boardName);
        BoardDto boardDto = modelMapper.map(board, BoardDto.class);

        return responseService.getSingleResult(boardDto);
    }

    @ApiOperation(value = "게시판 글 리스트", notes = "게시판 게시글 리스트를 조회한다.")
    @GetMapping(value = "/{boardName}/posts")
    public ListResult<PostResponseDto> posts(@PathVariable String boardName,
                                             @ApiIgnore @PageableDefault(page = 0, size = 10) Pageable pageable) {

        Page<Post> page = boardService.findPosts(boardName, pageable);
        List<PostResponseDto> postDtoList = page.stream()
                .map(p -> modelMapper.map(p, PostResponseDto.class))
                .collect(Collectors.toList());

        return responseService.getListResult(postDtoList, page);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "게시판 글 작성", notes = "게시판에 글을 작성한다.")
    @PostMapping(value = "/{boardName}")
    public SingleResult<PostResponseDto> post(@PathVariable String boardName, @Valid @ModelAttribute PostDto postDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uid = authentication.getName();

        Post post = boardService.writePost(uid, boardName, postDto);
        PostResponseDto postResponseDto = modelMapper.map(post, PostResponseDto.class);

        return responseService.getSingleResult(postResponseDto);
    }

    @ApiOperation(value = "게시판 글 상세", notes = "게시판 글 상세정보를 조회한다.")
    @GetMapping(value = "/post/{postId}")
    public SingleResult<PostResponseDto> post(@PathVariable long postId) {

        Post post = boardService.getPost(postId);
        PostResponseDto postDto = modelMapper.map(post, PostResponseDto.class);

        return responseService.getSingleResult(postDto);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "게시판 글 수정", notes = "게시판의 글을 수정한다.")
    @PutMapping(value = "/post/{postId}")
    public SingleResult<PostResponseDto> post(@PathVariable long postId, @Valid @ModelAttribute PostDto postDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uid = authentication.getName();

        Post post = boardService.updatePost(postId, uid, postDto);
        PostResponseDto postResponseDto = modelMapper.map(post, PostResponseDto.class);

        return responseService.getSingleResult(postResponseDto);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "게시판 글 삭제", notes = "게시판의 글을 삭제한다.")
    @DeleteMapping(value = "/post/{postId}")
    public CommonResult deletePost(@PathVariable long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uid = authentication.getName();
        boardService.deletePost(postId, uid);
        return responseService.getSuccessResult();
    }

    @ApiOperation(value = "인기순 글 리스트", notes = "전체 게시판 게시글 중 조회수가 가장 높은 10개 항목을 조회한다.")
    @GetMapping(value = "/post/views")
    public ListResult<PostResponseDto> postsByViews(
            @ApiIgnore @PageableDefault(page = 0, size = 10, sort = "views", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Post> page = boardService.findPosts(pageable);
        List<PostResponseDto> postDtoList = page.stream()
                .map(p -> modelMapper.map(p, PostResponseDto.class))
                .collect(Collectors.toList());

        return responseService.getListResult(postDtoList, page);
    }

    @ApiOperation(value = "최신 글 리스트", notes = "전체 게시판 게시글 중 최신순으로 10개 항목을 조회한다.")
    @GetMapping(value = "/post/new")
    public ListResult<PostResponseDto> postsByNew(
            @ApiIgnore @PageableDefault(page = 0, size = 10, sort = "modifiedAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Post> page = boardService.findPosts(pageable);
        List<PostResponseDto> postDtoList = page.stream()
                .map(p -> modelMapper.map(p, PostResponseDto.class))
                .collect(Collectors.toList());

        return responseService.getListResult(postDtoList, page);
    }

    @ApiOperation(value = "추천 글 리스트", notes = "전체 게시판 게시글 중 좋아요 순으로 10개 항목을 조회한다.")
    @GetMapping(value = "/post/likes")
    public ListResult<PostResponseDto> postsByLikes(
            @ApiIgnore @PageableDefault(page = 0, size = 10, sort = "likes", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Post> page = boardService.findPosts(pageable);
        List<PostResponseDto> postDtoList = page.stream()
                .map(p -> modelMapper.map(p, PostResponseDto.class))
                .collect(Collectors.toList());

        return responseService.getListResult(postDtoList, page);
    }
}