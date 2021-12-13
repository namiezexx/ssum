package com.kyobo.dev.api.Ssum.controller.board;

import com.kyobo.dev.api.Ssum.advice.exception.CUserNotFoundException;
import com.kyobo.dev.api.Ssum.entity.Post;
import com.kyobo.dev.api.Ssum.entity.User;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "게시글 상세 조회", notes = "게시글 상세정보를 조회한다.")
    @GetMapping(value = "/post/{postId}")
    public SingleResult<PostResponseDto> post(
            @ApiIgnore @AuthenticationPrincipal User user,
            @PathVariable long postId) {

        Post post = boardService.updateReadingHistory(user, postId);
        PostResponseDto postDto = modelMapper.map(post, PostResponseDto.class);

        return responseService.getSingleResult(postDto);
    }
    
    @ApiOperation(value = "특정 게시판 조회", notes = "특정 게시판의 글을 페이지 단위로 10개 조회한다.")
    @GetMapping(value = "/{boardName}/posts")
    public ListResult<PostResponseDto> posts(@PathVariable String boardName) {

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Post> page = boardService.findPosts(boardName, pageRequest);
        List<PostResponseDto> postDtoList = page.stream()
                .map(p -> modelMapper.map(p, PostResponseDto.class))
                .collect(Collectors.toList());

        return responseService.getListResult(postDtoList, page);
    }

    @ApiOperation(value = "인기순 글 리스트", notes = "전체 게시판 게시글 중 조회수가 가장 높은 10개 항목을 조회한다.")
    @GetMapping(value = "/post/views")
    public ListResult<PostResponseDto> postsByViews() {

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("views").descending());
        Page<Post> page = boardService.findPosts(pageRequest);
        List<PostResponseDto> postDtoList = page.stream()
                .map(p -> modelMapper.map(p, PostResponseDto.class))
                .collect(Collectors.toList());

        return responseService.getListResult(postDtoList, page);
    }

    @ApiOperation(value = "최신 글 리스트", notes = "전체 게시판 게시글 중 최신순으로 10개 항목을 조회한다.")
    @GetMapping(value = "/post/new")
    public ListResult<PostResponseDto> postsByNew() {

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("modifiedAt").descending());
        Page<Post> page = boardService.findPosts(pageRequest);
        List<PostResponseDto> postDtoList = page.stream()
                .map(p -> modelMapper.map(p, PostResponseDto.class))
                .collect(Collectors.toList());

        return responseService.getListResult(postDtoList, page);
    }

    @ApiOperation(value = "추천 글 리스트", notes = "전체 게시판 게시글 중 좋아요 순으로 10개 항목을 조회한다.")
    @GetMapping(value = "/post/likes")
    public ListResult<PostResponseDto> postsByLikes() {

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("likes").descending());
        Page<Post> page = boardService.findPosts(pageRequest);
        List<PostResponseDto> postDtoList = page.stream()
                .map(p -> modelMapper.map(p, PostResponseDto.class))
                .collect(Collectors.toList());

        return responseService.getListResult(postDtoList, page);
    }
}
