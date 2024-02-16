package com.backend.martall.domain.mart.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MartDetailResponseDto {
    private Long martshopId;
    private String name;
    private String ownerName;
    private String shopnumber;
    private String email;
    private String phonenumber;
    private String operatingHours;
    private String pickuptime;
    private String payment;
    private String address;
    private String kakaoTalkLink;
}
