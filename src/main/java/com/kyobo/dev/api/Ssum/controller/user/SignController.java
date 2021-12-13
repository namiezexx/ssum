package com.kyobo.dev.api.Ssum.controller.user;

import com.kyobo.dev.api.Ssum.advice.exception.CEmailSigninFailedException;
import com.kyobo.dev.api.Ssum.advice.exception.CUserExistException;
import com.kyobo.dev.api.Ssum.advice.exception.CUserNotFoundException;
import com.kyobo.dev.api.Ssum.config.security.JwtTokenProvider;
import com.kyobo.dev.api.Ssum.entity.User;
import com.kyobo.dev.api.Ssum.model.request.user.LoginDto;
import com.kyobo.dev.api.Ssum.model.request.user.JoinDto;
import com.kyobo.dev.api.Ssum.model.request.user.RefreshTokenDto;
import com.kyobo.dev.api.Ssum.model.response.CommonResult;
import com.kyobo.dev.api.Ssum.model.response.SingleResult;
import com.kyobo.dev.api.Ssum.model.response.user.TokenDto;
import com.kyobo.dev.api.Ssum.model.social.KakaoProfile;
import com.kyobo.dev.api.Ssum.model.social.SocialJoinDto;
import com.kyobo.dev.api.Ssum.model.social.SocialLoginDto;
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

    private final UserJpaRepo userJpaRepo;

    private final UserService userService;
    private final KakaoService kakaoService;
    private final ResponseService responseService;

    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @ApiOperation(value = "로그인", notes = "이메일 회원 로그인을 한다.")
    @PostMapping(value = "/signin")
    public SingleResult<TokenDto> signin(@ApiParam(value = "로그인 데이타", required = true) @RequestBody LoginDto loginDto) {

        User user = userService.findUser(loginDto.getId());
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword()))
            throw new CEmailSigninFailedException();

        TokenDto tokenDto = jwtTokenProvider.createToken(String.valueOf(user.getMsrl()), user.getRoles());

        // 사용자가 로그인을 하면 refreshToken을 DB에 보관하고 refreshToken 갱신 요청 시 비교하여 갱신처리한다.
        user.setRefreshToken(tokenDto.getRefreshToken());
        userJpaRepo.save(user);

        return responseService.getSingleResult(tokenDto);
    }

    @ApiOperation(value = "소셜 로그인", notes = "소셜 회원 로그인을 한다.")
    @PostMapping(value = "/signin/{provider}")
    public SingleResult<TokenDto> signinByProvider(
            @ApiParam(value = "서비스 제공자 provider", required = true, defaultValue = "kakao") @PathVariable String provider,
            @ApiParam(value = "소셜 access_token", required = true) @RequestBody SocialLoginDto socialLoginDto) {

        KakaoProfile profile = kakaoService.getKakaoProfile(socialLoginDto.getAccessToken());
        User user = Optional.ofNullable(userJpaRepo.findByUidAndProvider(String.valueOf(profile.getId()), provider)).orElseThrow(CUserNotFoundException::new);

        TokenDto tokenDto = jwtTokenProvider.createToken(String.valueOf(user.getMsrl()), user.getRoles());

        user.setRefreshToken(tokenDto.getRefreshToken());
        userJpaRepo.save(user);

        return responseService.getSingleResult(tokenDto);
    }

    @ApiOperation(value = "가입", notes = "회원가입을 한다.")
    @PostMapping(value = "/signup")
    public CommonResult signup(@ApiParam(value = "가입 데이타", required = true) @RequestBody JoinDto joinDto) {

        userService.checkUserPresent(joinDto.getId());

        userJpaRepo.save(User.builder()
                .uid(joinDto.getId())
                .password(passwordEncoder.encode(joinDto.getPassword()))
                .name(joinDto.getName())
                .phone(joinDto.getPhone())
                .profileImageUrl(joinDto.getProfileImageUrl())
                .roles(Collections.singletonList("ROLE_USER"))
                .build());
        return responseService.getSuccessResult();
    }

    @ApiOperation(value = "소셜 계정 가입", notes = "소셜 계정 회원가입을 한다.")
    @PostMapping(value = "/signup/{provider}")
    public CommonResult signupProvider(@ApiParam(value = "서비스 제공자 provider", required = true, defaultValue = "kakao") @PathVariable String provider,
                                       @ApiParam(value = "가입 데이타", required = true) @RequestBody SocialJoinDto socialJoinDto) {

        KakaoProfile profile = kakaoService.getKakaoProfile(socialJoinDto.getAccessToken());
        userService.checkSocialUserPresent(String.valueOf(profile.getId()), provider);

        userJpaRepo.save(User.builder()
                .uid(String.valueOf(profile.getId()))
                .provider(provider)
                .name(socialJoinDto.getName())
                .phone(socialJoinDto.getPhone())
                .profileImageUrl(socialJoinDto.getProfileImageUrl())
                .roles(Collections.singletonList("ROLE_USER"))
                .build());

        return responseService.getSuccessResult();
    }

    @ApiOperation(value = "accessToken 갱신", notes = "로그인 시 부여받은 refreshToken으로 accessToken을 갱신한다.")
    @PostMapping(value = "/signin/token")
    public SingleResult<TokenDto> tokenUpdate(@ApiParam(value = "토큰 갱신 데이타", required = true) @RequestBody RefreshTokenDto refreshTokenDto) {

        Authentication authentication = jwtTokenProvider.getAuthentication(refreshTokenDto.getRefreshToken());
        String uid = authentication.getName();

        User user = userService.findUser(uid);

        TokenDto tokenDto = jwtTokenProvider.createToken(String.valueOf(user.getMsrl()), user.getRoles());
        tokenDto.setRefreshToken(user.getRefreshToken());

        return responseService.getSingleResult(tokenDto);

    }
}