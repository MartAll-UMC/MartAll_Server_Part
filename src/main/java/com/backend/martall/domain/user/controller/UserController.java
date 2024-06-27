package com.backend.martall.domain.user.controller;

import com.backend.martall.domain.user.dto.AccountDto;
import com.backend.martall.domain.user.dto.JwtDto;
import com.backend.martall.domain.user.dto.UserDto;
import com.backend.martall.domain.user.jwt.JwtTokenProvider;
import com.backend.martall.domain.user.service.AccountService;
import com.backend.martall.domain.user.service.UserService;
import com.backend.martall.global.dto.JsonResponse;
import com.backend.martall.global.exception.ResponseStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "User API")
@Slf4j
@RequestMapping("/user")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AccountService accountService;

    @Operation(summary = "카카오 로그인")
    @ApiResponse(responseCode = "200", description = "로그인 성공", useReturnTypeSchema = true)
    @PostMapping("/login-kakao")
    public ResponseEntity<JsonResponse<JwtDto.TwoJwtDateDto>> joinUser(@RequestBody UserDto.UserKakaoRequestDto userKakaoRequestDto) {

        UserDto.UserRequestDto userRequestDto = new UserDto.UserRequestDto();
        userRequestDto.ofKakao(userKakaoRequestDto);

        JwtDto.TwoJwtDateDto token = userService.join(userRequestDto);

        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, token));
    }

    @Operation(summary = "회원가입")
    @ApiResponse(responseCode = "200", description = "회원가입 성공", useReturnTypeSchema = true)
    @PostMapping("/join")
    public ResponseEntity<JsonResponse> joinInAppUser(@Validated @RequestBody UserDto.UserJoinDto userJoinDto) {

        accountService.joinInApp(userJoinDto);

        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS));
    }

    @Operation(summary = "회원가입 아이디 중복체크")
    @ApiResponse(responseCode = "200", description = "아이디 중복체크 성공", useReturnTypeSchema = true)
    @GetMapping("/join/idDupCheck")
    public ResponseEntity<JsonResponse<UserDto.IdDupCheckResponseDto>> joinIdDupCheck(@Validated @RequestBody UserDto.IdDupCheckRequestDto idDupCheckRequestDto) {
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, accountService.joinIdDupCheck(idDupCheckRequestDto)));
    }


    @Operation(summary = "토큰 재발급")
    @ApiResponse(responseCode = "200", description = "토큰 재발급", useReturnTypeSchema = true)
    @GetMapping("/refresh")
    public ResponseEntity<JsonResponse<JwtDto.JwtDateDto>> refreshToken() {
        String refresh_token = jwtTokenProvider.resolveRefreshToken();

        JwtDto.JwtDateDto accessToken = userService.recreateAccessToken(refresh_token);

        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, accessToken));
    }


    @Operation(summary = "회원정보 조회")
    @ApiResponse(responseCode = "200", description = "회원정보 조회", useReturnTypeSchema = true)
    @GetMapping("/profile")
    public ResponseEntity<JsonResponse<UserDto.UserInfoResponseDto>> getProfile() {
        Long id = jwtTokenProvider.resolveToken();
        UserDto.UserInfoResponseDto userInfoResponseDto = userService.getUserInformation(id);

        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, userInfoResponseDto));
    }

    @Operation(summary = "로그아웃")
    @DeleteMapping("/logout")
    public ResponseEntity<JsonResponse> logoutUser() {
        Long id = jwtTokenProvider.resolveToken();
        userService.logoutUser(id);

        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }

    @Operation(summary = "위치 업데이트")
    @PatchMapping("/update-location")
    public ResponseEntity<JsonResponse> updateLocation(@RequestBody UserDto.UserLocationDto userLocationDto) {
        Long id = jwtTokenProvider.resolveToken();
        userService.updateLocation(id, userLocationDto);

        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }

    @Operation(summary = "범위 수정")
    @Parameter(name = "locationRange", description = "수정 범위")
    @PatchMapping("/change-range")
    public ResponseEntity<JsonResponse> changeRange(@RequestParam("locationRange") Integer locationRange) {
        Long id = jwtTokenProvider.resolveToken();
        userService.updateRange(id, locationRange);

        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }

    @Operation(summary = "위치 조회")
    @ApiResponse(responseCode = "200", description = "위치 조회", useReturnTypeSchema = true)
    @GetMapping("/location")
    public ResponseEntity<JsonResponse<UserDto.UserLocationDto>> getLocation() {
        Long id = jwtTokenProvider.resolveToken();
        UserDto.UserLocationDto userLocationDto = userService.getLocation(id);

        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, userLocationDto));
    }

    @Operation(summary = "위치 및 범위 조회")
    @ApiResponse(responseCode = "200", description = "위치 및 범위 조회", useReturnTypeSchema = true)
    @GetMapping("/location-range")
    public ResponseEntity<JsonResponse<UserDto.UserLocationRangeDto>> getLocationRange() {
        Long id = jwtTokenProvider.resolveToken();
        UserDto.UserLocationRangeDto userLocationRangeDto = userService.getLocationRange(id);

        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, userLocationRangeDto));
    }


    @Operation(summary = "아이디 찾기 이메일 인증코드 요청")
    @ApiResponse(responseCode = "200", description = "이메일 인증 요청 성공", useReturnTypeSchema = true)
    @PostMapping("/idInquiry/email")
    public ResponseEntity<JsonResponse> requestIdInquiryEmailCertification(@Validated @RequestBody AccountDto.IdInquiryEmailRequestDto idInquiryEmailRequestDto) {

        accountService.requestEmailCertification(idInquiryEmailRequestDto);

        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS));
    }

    @Operation(summary = "아이디 찾기 이메일 인증코드 입력")
    @ApiResponse(responseCode = "200", description = "이메일 인증 성공", useReturnTypeSchema = true)
    @GetMapping("/idInquiry/email")
    public ResponseEntity<JsonResponse<AccountDto.IdInquiryCertificationCodeResponseDto>> responseIdInquiryEmailCertification(
            @Validated @RequestBody AccountDto.IdInquiryCertificationCodeRequestDto idInquiryCertificationCodeRequestDto) {

        AccountDto.IdInquiryCertificationCodeResponseDto idInquiryCertificationCodeResponseDto
                = accountService.responseEmailCertification(idInquiryCertificationCodeRequestDto);

        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, idInquiryCertificationCodeResponseDto));
    }

    @Operation(summary = "아이디 찾기")
    @ApiResponse(responseCode = "200", description = "아이디 찾기 성공", useReturnTypeSchema = true)
    @GetMapping("/idInquiry")
    public ResponseEntity<JsonResponse<AccountDto.IdInquiryResponseDto>> idInquiry(
            @Validated @RequestBody AccountDto.IdInquiryRequestDto idInquiryRequestDto) {

        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, accountService.idInquiry(idInquiryRequestDto)));
    }
}
