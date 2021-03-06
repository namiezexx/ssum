package com.kyobo.dev.api.Ssum.controller.user;

import com.kyobo.dev.api.Ssum.entity.User;
import com.kyobo.dev.api.Ssum.dto.request.user.UpdateDto;
import com.kyobo.dev.api.Ssum.dto.response.CommonResult;
import com.kyobo.dev.api.Ssum.dto.response.ListResult;
import com.kyobo.dev.api.Ssum.dto.response.SingleResult;
import com.kyobo.dev.api.Ssum.dto.response.user.UserDto;
import com.kyobo.dev.api.Ssum.service.ResponseService;
import com.kyobo.dev.api.Ssum.service.user.UserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = {"2. User"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class UserController {

    private final UserService userService;
    private final ResponseService responseService;

    private final ModelMapper modelMapper;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 리스트 조회", notes = "모든 회원을 조회한다")
    @GetMapping(value = "/users/{page}")
    public ListResult<UserDto> findAllUser(@PathVariable("page") Integer page) {

        PageRequest pageRequest = PageRequest.of(page, 10);
        Page<User> pages = userService.findUsers(pageRequest);
        List<UserDto> userDtoList = pages.stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());

        return responseService.getListResult(userDtoList, pages);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 단건 조회", notes = "회원번호(userId)로 회원을 조회한다")
    @GetMapping(value = "/user")
    public SingleResult<UserDto> findUser(@ApiIgnore @AuthenticationPrincipal User user) {

        // Spring Security 에서 accessToken 으로 이미 회원조회를 완료하고 AuthenticationPricipal Annotation 을 통해서 User 정보를 가져온다.
        UserDto userDto = modelMapper.map(user, UserDto.class);

        return responseService.getSingleResult(userDto);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 수정", notes = "회원정보를 수정한다")
    @PutMapping(value = "/user")
    public SingleResult<UserDto> modify(
            @ApiIgnore @AuthenticationPrincipal User user,
            @ApiParam(value = "회원이름", required = true) @RequestBody UpdateDto updateDto) {

        user.setName(updateDto.getName());
        user.setPhone(updateDto.getPhone());
        user.setProfileImageUrl(updateDto.getProfileImageUrl());
        user = userService.updateUser(user);

        UserDto userDto = modelMapper.map(user, UserDto.class);

        return responseService.getSingleResult(userDto);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 삭제", notes = "회원번호(msrl)로 회원정보를 삭제한다")
    @DeleteMapping(value = "/user")
    public CommonResult delete(
            @ApiIgnore @AuthenticationPrincipal User user
    ) {

        userService.deleteUser(user);
        return responseService.getSuccessResult();
    }
}
