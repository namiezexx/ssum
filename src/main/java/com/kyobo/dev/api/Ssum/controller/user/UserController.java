package com.kyobo.dev.api.Ssum.controller.user;

import com.kyobo.dev.api.Ssum.advice.exception.CUserNotFoundException;
import com.kyobo.dev.api.Ssum.entity.User;
import com.kyobo.dev.api.Ssum.model.request.user.UpdateDto;
import com.kyobo.dev.api.Ssum.model.response.CommonResult;
import com.kyobo.dev.api.Ssum.model.response.ListResult;
import com.kyobo.dev.api.Ssum.model.response.SingleResult;
import com.kyobo.dev.api.Ssum.model.response.user.UserDto;
import com.kyobo.dev.api.Ssum.repository.UserJpaRepo;
import com.kyobo.dev.api.Ssum.service.ResponseService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = {"2. User"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class UserController {

    private final UserJpaRepo userJpaRepo;
    private final ResponseService responseService; // 결과를 처리할 Service

    private final ModelMapper modelMapper;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 리스트 조회", notes = "모든 회원을 조회한다")
    @GetMapping(value = "/users")
    public ListResult<UserDto> findAllUser(@ApiIgnore @PageableDefault(page = 0, size = 10) Pageable pageable) {

        Page<User> page = userJpaRepo.findAll(pageable);
        List<UserDto> userDtoList = page.stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());

        return responseService.getListResult(userDtoList, page);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 단건 조회", notes = "회원번호(msrl)로 회원을 조회한다")
    @GetMapping(value = "/user")
    public SingleResult<UserDto> findUser() {

        // SecurityContext에서 인증받은 회원의 정보를 얻어온다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();

        User user = userJpaRepo.findByUid(id).orElseThrow(CUserNotFoundException::new);
        UserDto userDto = modelMapper.map(user, UserDto.class);

        return responseService.getSingleResult(userDto);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 수정", notes = "회원정보를 수정한다")
    @PutMapping(value = "/user")
    public SingleResult<UserDto> modify(
            @ApiParam(value = "회원이름", required = true) @RequestBody UpdateDto updateDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();

        User user = userJpaRepo.findByUid(id).orElseThrow(CUserNotFoundException::new);
        user.setName(updateDto.getName());

        /**
         * Controller는 Trasactional이 걸려있지 않으므로 직접 save 메소드를 사용해야한다.
         */
        user = userJpaRepo.save(user);
        UserDto userDto = modelMapper.map(user, UserDto.class);

        return responseService.getSingleResult(userDto);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 삭제", notes = "회원번호(msrl)로 회원정보를 삭제한다")
    @DeleteMapping(value = "/user/{msrl}")
    public CommonResult delete(
            @ApiParam(value = "회원번호", required = true) @PathVariable long msrl) {

        userJpaRepo.deleteById(msrl);

        return responseService.getSuccessResult();
    }
}
