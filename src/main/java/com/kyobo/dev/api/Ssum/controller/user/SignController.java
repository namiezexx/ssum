package com.kyobo.dev.api.Ssum.controller.user;

import com.kyobo.dev.api.Ssum.advice.exception.CEmailSigninFailedException;
import com.kyobo.dev.api.Ssum.advice.exception.CUserNotFoundException;
import com.kyobo.dev.api.Ssum.config.security.JwtTokenProvider;
import com.kyobo.dev.api.Ssum.entity.User;
import com.kyobo.dev.api.Ssum.dto.request.user.LoginDto;
import com.kyobo.dev.api.Ssum.dto.request.user.JoinDto;
import com.kyobo.dev.api.Ssum.dto.request.user.RefreshTokenDto;
import com.kyobo.dev.api.Ssum.dto.response.CommonResult;
import com.kyobo.dev.api.Ssum.dto.response.SingleResult;
import com.kyobo.dev.api.Ssum.dto.response.user.TokenDto;
import com.kyobo.dev.api.Ssum.dto.social.KakaoProfile;
import com.kyobo.dev.api.Ssum.dto.social.SocialJoinDto;
import com.kyobo.dev.api.Ssum.dto.social.SocialLoginDto;
import com.kyobo.dev.api.Ssum.repository.UserJpaRepo;
import com.kyobo.dev.api.Ssum.service.ResponseService;
import com.kyobo.dev.api.Ssum.service.user.UserService;
import com.kyobo.dev.api.Ssum.service.social.KakaoService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@Api(tags = {"1. Sign"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
@CrossOrigin(origins = "http://localhost:8080")
public class SignController {

    private final UserService userService;
    private final KakaoService kakaoService;
    private final ResponseService responseService;

    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @ApiOperation(value = "로그인", notes = "이메일 회원 로그인을 한다.")
    @PostMapping(value = "/login")
    public SingleResult<TokenDto> login(@ApiParam(value = "로그인 데이타", required = true) @RequestBody LoginDto loginDto) {

        User user = userService.findUserByEmail(loginDto.getEmail());
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword()))
            throw new CEmailSigninFailedException();

        TokenDto tokenDto = jwtTokenProvider.createToken(String.valueOf(user.getUserId()), user.getRoles());

        // 사용자가 로그인을 하면 refreshToken을 DB에 보관하고 refreshToken 갱신 요청 시 비교하여 갱신처리한다.
        user.setRefreshToken(tokenDto.getRefreshToken());
        userService.updateUser(user);

        return responseService.getSingleResult(tokenDto);
    }

    @ApiOperation(value = "소셜 로그인", notes = "소셜 회원 로그인을 한다.")
    @PostMapping(value = "/login/{provider}")
    public SingleResult<TokenDto> loginByProvider(
            @ApiParam(value = "서비스 제공자 provider", required = true, defaultValue = "kakao") @PathVariable String provider,
            @ApiParam(value = "소셜 access_token", required = true) @RequestBody SocialLoginDto socialLoginDto) {

        KakaoProfile profile = kakaoService.getKakaoProfile(socialLoginDto.getAccessToken());
        User user = userService.findByEmailAndProvider(String.valueOf(profile.getId()), provider);

        TokenDto tokenDto = jwtTokenProvider.createToken(String.valueOf(user.getUserId()), user.getRoles());

        user.setRefreshToken(tokenDto.getRefreshToken());
        userService.updateUser(user);

        return responseService.getSingleResult(tokenDto);
    }

    @ApiOperation(value = "가입", notes = "회원가입을 한다.")
    @PostMapping(value = "/join")
    public CommonResult join(@ApiParam(value = "가입 데이타", required = true) @RequestBody JoinDto joinDto) {

        userService.checkUserPresentByEmail(joinDto.getEmail());

        userService.updateUser(User.builder()
                .email(joinDto.getEmail())
                .password(passwordEncoder.encode(joinDto.getPassword()))
                .name(joinDto.getName())
                .phone(joinDto.getPhone())
                .profileImageUrl(joinDto.getProfileImageUrl())
                .roles(Collections.singletonList("ROLE_USER"))
                .build());
        return responseService.getSuccessResult();
    }

    @ApiOperation(value = "소셜 계정 가입", notes = "소셜 계정 회원가입을 한다.")
    @PostMapping(value = "/join/{provider}")
    public CommonResult signupProvider(@ApiParam(value = "서비스 제공자 provider", required = true, defaultValue = "kakao") @PathVariable String provider,
                                       @ApiParam(value = "가입 데이타", required = true) @RequestBody SocialJoinDto socialJoinDto) {

        KakaoProfile profile = kakaoService.getKakaoProfile(socialJoinDto.getAccessToken());
        userService.checkSocialUserPresentBySocialId(String.valueOf(profile.getId()), provider);

        userService.updateUser(User.builder()
                .email(String.valueOf(profile.getId()))
                .provider(provider)
                .name(socialJoinDto.getName())
                .phone(socialJoinDto.getPhone())
                .profileImageUrl(socialJoinDto.getProfileImageUrl())
                .roles(Collections.singletonList("ROLE_USER"))
                .build());

        return responseService.getSuccessResult();
    }

    @ApiOperation(value = "accessToken 갱신", notes = "로그인 시 부여받은 refreshToken으로 accessToken을 갱신한다.")
    @PostMapping(value = "/refresh/token")
    public SingleResult<TokenDto> tokenUpdate(
            @ApiParam(value = "토큰 갱신 데이타", required = true) @RequestBody RefreshTokenDto refreshTokenDto) {

        Authentication authentication = jwtTokenProvider.getAuthentication(refreshTokenDto.getRefreshToken());
        String email = authentication.getName();

        User user = userService.findUserByEmail(email);

        TokenDto tokenDto = jwtTokenProvider.createToken(String.valueOf(user.getUserId()), user.getRoles());
        tokenDto.setRefreshToken(user.getRefreshToken());

        return responseService.getSingleResult(tokenDto);

    }
}