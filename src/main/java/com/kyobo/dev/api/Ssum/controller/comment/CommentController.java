package com.kyobo.dev.api.Ssum.controller.comment;

import com.kyobo.dev.api.Ssum.entity.Comment;
import com.kyobo.dev.api.Ssum.entity.User;
import com.kyobo.dev.api.Ssum.model.request.comment.CommentRequestDto;
import com.kyobo.dev.api.Ssum.model.response.CommonResult;
import com.kyobo.dev.api.Ssum.model.response.ListResult;
import com.kyobo.dev.api.Ssum.model.response.SingleResult;
import com.kyobo.dev.api.Ssum.model.response.comment.CommentResponseDto;
import com.kyobo.dev.api.Ssum.service.ResponseService;
import com.kyobo.dev.api.Ssum.service.comment.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = {"5. Comment"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/board")
public class CommentController {

    private final CommentService commentsService;
    private final ResponseService responseService;

    private final ModelMapper modelMapper;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "댓글 작성", notes = "게시글에 댓글을 작성한다.")
    @PostMapping(value = "/comment/{postId}")
    public SingleResult<CommentResponseDto> comment(
            @ApiIgnore @AuthenticationPrincipal User user,
            @PathVariable long postId,
            @RequestBody CommentRequestDto commentRequestDto) {

        Comment comment = commentsService.addComment(commentRequestDto.getContents(), user, postId);
        CommentResponseDto commentResponseDto = modelMapper.map(comment, CommentResponseDto.class);

        return responseService.getSingleResult(commentResponseDto);
    }

    @ApiOperation(value = "댓글 조회", notes = "게시글에 댓글을 조회한다.")
    @GetMapping(value = "/comment/{postId}")
    public ListResult<CommentResponseDto> comments(@PathVariable long postId) {

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Comment> comments = commentsService.findComments(postId, pageRequest);

        List<CommentResponseDto> commentResponseDtoList = comments.stream()
                .map(c -> modelMapper.map(c, CommentResponseDto.class))
                .collect(Collectors.toList());

        return responseService.getListResult(commentResponseDtoList, comments);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "댓글 수정", notes = "게시판의 댓글을 수정한다.")
    @PutMapping(value = "/comment/{commentId}")
    public SingleResult<CommentResponseDto> updateComment(
            @ApiIgnore @AuthenticationPrincipal User user,
            @PathVariable long commentId,
            @RequestBody CommentRequestDto commentRequestDto) {

        Comment comment = commentsService.updateComment(user, commentId, commentRequestDto);
        CommentResponseDto commentResponseDto = modelMapper.map(comment, CommentResponseDto.class);

        return responseService.getSingleResult(commentResponseDto);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "댓글 삭제", notes = "게시판의 댓글을 삭제한다.")
    @DeleteMapping(value = "/comment/{commentId}")
    public CommonResult deleteComment(
            @ApiIgnore @AuthenticationPrincipal User user,
            @PathVariable long commentId) {

        commentsService.deleteComment(user, commentId);

        return responseService.getSuccessResult();
    }
}
