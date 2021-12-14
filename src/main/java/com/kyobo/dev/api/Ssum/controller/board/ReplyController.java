package com.kyobo.dev.api.Ssum.controller.board;

import com.kyobo.dev.api.Ssum.entity.Reply;
import com.kyobo.dev.api.Ssum.entity.User;
import com.kyobo.dev.api.Ssum.model.request.board.ReplyRequestDto;
import com.kyobo.dev.api.Ssum.model.response.SingleResult;
import com.kyobo.dev.api.Ssum.model.response.board.ReplyResponseDto;
import com.kyobo.dev.api.Ssum.service.ResponseService;
import com.kyobo.dev.api.Ssum.service.board.ReplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = {"5. Reply"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/board")
public class ReplyController {

    private final ReplyService replyService;
    private final ResponseService responseService;

    private final ModelMapper modelMapper;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "댓글 작성", notes = "게시글에 댓글을 작성한다.")
    @PostMapping(value = "/reply/{postId}")
    public SingleResult<ReplyResponseDto> post(
            @ApiIgnore @AuthenticationPrincipal User user,
            @PathVariable long postId,
            @RequestBody ReplyRequestDto replyRequestDto) {

        Reply reply = replyService.addReply(replyRequestDto.getContents(), user, postId);
        ReplyResponseDto replyResponseDto = modelMapper.map(reply, ReplyResponseDto.class);

        return responseService.getSingleResult(replyResponseDto);
    }
}
