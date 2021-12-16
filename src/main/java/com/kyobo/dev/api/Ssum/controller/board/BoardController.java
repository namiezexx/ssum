package com.kyobo.dev.api.Ssum.controller.board;

import com.kyobo.dev.api.Ssum.entity.Board;
import com.kyobo.dev.api.Ssum.entity.Post;
import com.kyobo.dev.api.Ssum.entity.User;
import com.kyobo.dev.api.Ssum.dto.response.board.BoardDto;
import com.kyobo.dev.api.Ssum.dto.request.board.PostDto;
import com.kyobo.dev.api.Ssum.dto.response.CommonResult;
import com.kyobo.dev.api.Ssum.dto.response.SingleResult;
import com.kyobo.dev.api.Ssum.dto.response.board.PostResponseDto;
import com.kyobo.dev.api.Ssum.service.ResponseService;
import com.kyobo.dev.api.Ssum.service.board.BoardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

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

        /**
         * Controller는 UI와 Service사이의 데이터 변환을 담당하고 실제 비즈니스 로직은 Service에서 처리한다.
         */

        Board board = boardService.findBoard(boardName);
        BoardDto boardDto = modelMapper.map(board, BoardDto.class);

        return responseService.getSingleResult(boardDto);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "게시판 글 작성", notes = "게시판에 글을 작성한다.")
    @PostMapping(value = "/{boardName}")
    public SingleResult<PostResponseDto> post(
            @ApiIgnore @AuthenticationPrincipal User user,
            @PathVariable String boardName,
            @Valid @RequestBody PostDto postDto) {

        Post post = boardService.writePost(user, boardName, postDto);
        PostResponseDto postResponseDto = modelMapper.map(post, PostResponseDto.class);

        return responseService.getSingleResult(postResponseDto);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "게시판 글 수정", notes = "게시판의 글을 수정한다.")
    @PutMapping(value = "/post/{postId}")
    public SingleResult<PostResponseDto> post(
            @ApiIgnore @AuthenticationPrincipal User user,
            @PathVariable long postId,
            @Valid @RequestBody PostDto postDto) {

        Post post = boardService.updatePost(user, postId, postDto);
        PostResponseDto postResponseDto = modelMapper.map(post, PostResponseDto.class);

        return responseService.getSingleResult(postResponseDto);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "게시판 글 삭제", notes = "게시판의 글을 삭제한다.")
    @DeleteMapping(value = "/post/{postId}")
    public CommonResult deletePost(
            @ApiIgnore @AuthenticationPrincipal User user,
            @PathVariable long postId) {

        boardService.deletePost(user, postId);

        return responseService.getSuccessResult();
    }
}