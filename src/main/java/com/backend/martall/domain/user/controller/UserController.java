package com.backend.martall.domain.user.controller;

import com.backend.martall.domain.user.entity.User;
import com.backend.martall.domain.user.entity.UserRepository;
import com.backend.martall.global.dto.JsonResponse;
import com.backend.martall.global.exception.GlobalException;
import com.backend.martall.global.exception.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequestMapping("/user")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserRepository userRepository;

    @PostMapping("/s1") //success with data(temporary null)
    public ResponseEntity<JsonResponse> test1(){

        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }

    @PostMapping("/s2") //error return
    public ResponseEntity<JsonResponse> test3(){
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.RESPONSE_ERROR));
    }

    @PostMapping("/s3") //error detected
    public ResponseEntity<JsonResponse> test4(){

        if(true == true)
            throw new GlobalException(ResponseStatus.DATABASE_ERROR);

        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }
}
