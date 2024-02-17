package com.backend.martall.domain.mart.dto;
import com.backend.martall.domain.mart.entity.MartCategory;
import com.backend.martall.domain.mart.entity.MartBookmark;
import com.backend.martall.domain.mart.entity.MartShop;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class FollowedMartResponseDto {
    private Long bookmarkId;
    private Long martShopId;
    private String martName;
    private List<String> martCategory;
    private int likeCount;
    private int bookmarkCount;
    private boolean isFollowed = true;

}
