package com.backend.martall.domain.user.controller;

import com.backend.martall.domain.item.dto.ItemKeywordSearchResponseDto;
import com.backend.martall.domain.user.dto.JwtDto;
import com.backend.martall.domain.user.dto.UserDto;
import com.backend.martall.domain.user.jwt.JwtTokenProvider;
import com.backend.martall.domain.user.service.UserService;
import com.backend.martall.global.dto.JsonResponse;
import com.backend.martall.global.exception.ResponseStatus;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "User API")
@Slf4j
@RequestMapping("/user")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "카카오 로그인")
    @ApiResponse(responseCode = "200", description = "로그인 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = JwtDto.TwoJwtDateDto.class)))
    @PostMapping("/login-kakao")
    public ResponseEntity<JsonResponse> joinUser(@RequestBody UserDto.UserRequestDto userRequestDto) {

        JwtDto.TwoJwtDateDto token = userService.join(userRequestDto);

        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, token));
    }

    @Operation(summary = "토큰 재발급")
    @ApiResponse(responseCode = "200", description = "토큰 재발급",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = JwtDto.JwtDateDto.class)))
    @GetMapping("/refresh")
    public ResponseEntity<JsonResponse> refreshToken() {
        String refresh_token = jwtTokenProvider.resolveRefreshToken();

        JwtDto.JwtDateDto accessToken = userService.recreateAccessToken(refresh_token);

        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, accessToken));
    }


    @Operation(summary = "회원정보 조회")
    @ApiResponse(responseCode = "200", description = "회원정보 조회",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDto.UserInfoResponseDto.class)))
    @GetMapping("/profile")
    public ResponseEntity<JsonResponse> getProfile() {
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
    @ApiResponse(responseCode = "200", description = "위치 조회",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = UserDto.UserLocationDto.class))))
    @GetMapping("/location")
    public ResponseEntity<JsonResponse> getLocation() {
        Long id = jwtTokenProvider.resolveToken();
        UserDto.UserLocationDto userLocationDto = userService.getLocation(id);

        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, userLocationDto));
    }

    @Operation(summary = "위치 및 범위 조회")
    @ApiResponse(responseCode = "200", description = "위치 및 범위 조회",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = UserDto.UserLocationRangeDto.class))))
    @GetMapping("/location-range")
    public ResponseEntity<JsonResponse> getLocationRange() {
        Long id = jwtTokenProvider.resolveToken();
        UserDto.UserLocationRangeDto userLocationRangeDto = userService.getLocationRange(id);

        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, userLocationRangeDto));
    }
}
