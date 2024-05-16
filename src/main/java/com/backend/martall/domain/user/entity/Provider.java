package com.backend.martall.domain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Provider {
    KAKAO("kakao"),
    IN_APP("inapp")
    ;

    private final String name;

}
