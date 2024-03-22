package com.backend.martall.domain.mart.dto;

import com.backend.martall.domain.item.dto.ItemMartNewResponseDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MartRecommendedResponseDto {
    private Long martId;
    private String martImg;
    private String martName;
    private List<String> martCategory;
}
