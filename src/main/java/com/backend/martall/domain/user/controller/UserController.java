package com.backend.martall.domain.user.controller;

import com.backend.martall.domain.user.dto.UserDto;
import com.backend.martall.domain.user.entity.User;
import com.backend.martall.domain.user.entity.UserRepository;
import com.backend.martall.domain.user.jwt.JwtTokenProvider;
import com.backend.martall.domain.user.service.UserService;
import com.backend.martall.global.dto.JsonResponse;
import com.backend.martall.global.exception.GlobalException;
import com.backend.martall.global.exception.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequestMapping("/user")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/join")
    public ResponseEntity<JsonResponse> joinUser(@RequestBody UserDto.UserRequestDto userRequestDto) {

        String token = userService.join(userRequestDto);

        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, token));
    }

    @GetMapping("/test-jwt")
    public ResponseEntity<JsonResponse> joinUser() {

        Long Id = jwtTokenProvider.resolveToken();

        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, Id));
    }
}
