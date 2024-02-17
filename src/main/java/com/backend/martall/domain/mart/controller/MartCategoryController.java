package com.backend.martall.domain.mart.controller;

import com.backend.martall.domain.mart.dto.CategoryCreateRequestDto;
import com.backend.martall.domain.mart.dto.MartRequestDto;
import com.backend.martall.domain.mart.service.MartCategoryService;
import com.backend.martall.global.dto.JsonResponse;
import com.backend.martall.global.exception.ResponseStatus;
import jdk.jfr.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MartCategoryController {

    private final MartCategoryService martCategoryService;

    @PostMapping("/mart/category/create")
    public ResponseEntity<JsonResponse> createCategory(@RequestBody CategoryCreateRequestDto categoryCreateRequestDto) {
        martCategoryService.createCategory(categoryCreateRequestDto);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }

}
