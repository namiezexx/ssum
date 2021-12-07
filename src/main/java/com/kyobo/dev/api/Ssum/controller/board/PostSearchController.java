package com.kyobo.dev.api.Ssum.controller.board;

import com.kyobo.dev.api.Ssum.entity.Post;
import com.kyobo.dev.api.Ssum.model.response.ListResult;
import com.kyobo.dev.api.Ssum.model.response.SingleResult;
import com.kyobo.dev.api.Ssum.model.response.board.PostResponseDto;
import com.kyobo.dev.api.Ssum.service.ResponseService;
import com.kyobo.dev.api.Ssum.service.board.BoardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = {"4. Search"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/board")
public class PostSearchController {

    private final BoardService boardService;
    private final ResponseService responseService;

    private final ModelMapper modelMapper;

    @ApiOperation(value = "게시글 상세 조회", notes = "게시글 상세정보를 조회한다.")
    @GetMapping(value = "/post/{postId}")
    public SingleResult<PostResponseDto> post(@PathVariable long postId) {

        Post post = boardService.getPost(postId);
        PostResponseDto postDto = modelMapper.map(post, PostResponseDto.class);

        return responseService.getSingleResult(postDto);
    }
    
    @ApiOperation(value = "특정 게시판 조회", notes = "특정 게시판의 글을 페이지 단위로 10개 조회한다.")
    @GetMapping(value = "/{boardName}/posts")
    public ListResult<PostResponseDto> posts(@PathVariable String boardName,
                                             @ApiIgnore @PageableDefault(page = 0, size = 10) Pageable pageable) {

        Page<Post> page = boardService.findPosts(boardName, pageable);
        List<PostResponseDto> postDtoList = page.stream()
                .map(p -> modelMapper.map(p, PostResponseDto.class))
                .collect(Collectors.toList());

        return responseService.getListResult(postDtoList, page);
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
