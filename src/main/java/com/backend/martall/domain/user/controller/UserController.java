package com.backend.martall.domain.user.controller;

import com.backend.martall.domain.user.dto.UserDto;
import com.backend.martall.domain.user.jwt.JwtTokenProvider;
import com.backend.martall.domain.user.service.UserService;
import com.backend.martall.global.dto.JsonResponse;
import com.backend.martall.global.exception.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/user")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login-kakao")
    public ResponseEntity<JsonResponse> joinUser(@RequestBody UserDto.UserRequestDto userRequestDto) {

        String token = userService.join(userRequestDto);

        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, token));
    }


    @GetMapping("/profile")
    public ResponseEntity<JsonResponse> getProfile() {
        Long id = jwtTokenProvider.resolveToken();
        UserDto.UserInfoResponseDto userInfoResponseDto = userService.getUserInformation(id);

        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, userInfoResponseDto));
    }

    @DeleteMapping("/logout")
    public ResponseEntity<JsonResponse> logoutUser() {
        Long id = jwtTokenProvider.resolveToken();
        userService.logoutUser(id);

        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }

    @PatchMapping("/update-location")
    public ResponseEntity<JsonResponse> updateLocation(@RequestBody UserDto.UserLocationDto userLocationDto) {
        Long id = jwtTokenProvider.resolveToken();
        userService.updateLocation(id, userLocationDto);

        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }

    @PatchMapping("/change-range")
    public ResponseEntity<JsonResponse> changeRange(@RequestParam("locationRange") Integer locationRange) {
        Long id = jwtTokenProvider.resolveToken();
        userService.updateRange(id, locationRange);

        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }

    @GetMapping("/location")
    public ResponseEntity<JsonResponse> getLocation() {
        Long id = jwtTokenProvider.resolveToken();
        UserDto.UserLocationDto userLocationDto = userService.getLocation(id);

        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, userLocationDto));
    }

    @GetMapping("/location-range")
    public ResponseEntity<JsonResponse> getLocationRange() {
        Long id = jwtTokenProvider.resolveToken();
        UserDto.UserLocationRangeDto userLocationRangeDto = userService.getLocationRange(id);

        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, userLocationRangeDto));
    }
}
