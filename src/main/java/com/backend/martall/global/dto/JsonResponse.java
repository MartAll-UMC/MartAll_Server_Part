package com.backend.martall.global.dto;

import com.backend.martall.global.exception.ResponseStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class JsonResponse<T> {
    private final String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    private boolean isSuccess;
    private int status;
    private String message;
    private Object result;

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
