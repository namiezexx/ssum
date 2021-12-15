package com.kyobo.dev.api.Ssum.controller.comment;

import com.kyobo.dev.api.Ssum.entity.NestedComment;
import com.kyobo.dev.api.Ssum.entity.User;
import com.kyobo.dev.api.Ssum.model.request.comment.NestedCommentRequestDto;
import com.kyobo.dev.api.Ssum.model.response.CommonResult;
import com.kyobo.dev.api.Ssum.model.response.ListResult;
import com.kyobo.dev.api.Ssum.model.response.SingleResult;
import com.kyobo.dev.api.Ssum.model.response.comment.NestedCommentResponseDto;
import com.kyobo.dev.api.Ssum.service.ResponseService;
import com.kyobo.dev.api.Ssum.service.comment.NestedCommentService;
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

@Api(tags = {"6. NestedComment"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/board")
public class NestedCommentController {

    private final NestedCommentService nestedCommentsService;
    private final ResponseService responseService;

    private final ModelMapper modelMapper;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "대댓글 작성", notes = "댓글에 대댓글을 작성한다.")
    @PostMapping(value = "/nested/comment/{commentId}")
    public SingleResult<NestedCommentResponseDto> nestedComment(
            @ApiIgnore @AuthenticationPrincipal User user,
            @PathVariable long commentId,
            @RequestBody NestedCommentRequestDto nestedCommentRequestDto) {

        NestedComment nestedComment = nestedCommentsService.addNestedComment(nestedCommentRequestDto.getContents(), user, commentId);
        NestedCommentResponseDto nestedCommentResponseDto = modelMapper.map(nestedComment, NestedCommentResponseDto.class);

        return responseService.getSingleResult(nestedCommentResponseDto);
    }

    @ApiOperation(value = "대댓글 조회", notes = "댓글에 대댓글을 조회한다.")
    @GetMapping(value = "/nested/comment/{commentId}")
    public ListResult<NestedCommentResponseDto> comments(@PathVariable long commentId) {

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<NestedComment> nestedComments = nestedCommentsService.findNestedComments(commentId, pageRequest);

        List<NestedCommentResponseDto> nestedCommentResponseDtoList = nestedComments.stream()
                .map(c -> modelMapper.map(c, NestedCommentResponseDto.class))
                .collect(Collectors.toList());

        return responseService.getListResult(nestedCommentResponseDtoList, nestedComments);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "대댓글 수정", notes = "댓글의 대댓글을 수정한다.")
    @PutMapping(value = "/nested/comment/{nestedCommentId}")
    public SingleResult<NestedCommentResponseDto> updateNestedComment(
            @ApiIgnore @AuthenticationPrincipal User user,
            @PathVariable long nestedCommentId,
            @RequestBody NestedCommentRequestDto nestedCommentRequestDto) {

        NestedComment nestedComment = nestedCommentsService.updateNestedComment(user, nestedCommentId, nestedCommentRequestDto);
        NestedCommentResponseDto nestedCommentResponseDto = modelMapper.map(nestedComment, NestedCommentResponseDto.class);

        return responseService.getSingleResult(nestedCommentResponseDto);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "대댓글 삭제", notes = "댓글의 대댓글을 삭제한다.")
    @DeleteMapping(value = "/nested/comment/{nestedCommentId}")
    public CommonResult deleteNestedComment(
            @ApiIgnore @AuthenticationPrincipal User user,
            @PathVariable long nestedCommentId) {

        nestedCommentsService.deleteNestedComment(user, nestedCommentId);

        return responseService.getSuccessResult();
    }
}
