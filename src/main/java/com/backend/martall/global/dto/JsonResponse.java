package com.backend.martall.global.dto;

import com.backend.martall.global.exception.ResponseStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class JsonResponse<T> {
    @Schema(example = "2024-03-25 19:22:59")
    private final String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    @Schema(example = "true")
    private boolean isSuccess;

    @Schema(example = "200")
    private int status;

    @Schema(example = "요청이 성공했습니다")
    private String message;

    private T result;

    // 요청 성공
    public JsonResponse(ResponseStatus status, T result){
        this.isSuccess = status.isSuccess();
        this.message = status.getMessage();
        this.status = status.getCode();
        this.result = result;
    }

    // 요청 실패
    public JsonResponse(ResponseStatus status){
        this.isSuccess = status.isSuccess();
        this.message = status.getMessage();
        this.status = status.getCode();
        this.result = null;
    }
}
