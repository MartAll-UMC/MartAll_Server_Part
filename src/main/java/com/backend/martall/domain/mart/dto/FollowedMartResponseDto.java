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
    private Long martshopId;
    private String martname;
    private List<String> martcategory;
    private int visitorCount;
    private int salesIndex;
    private boolean isfollowed = true;


    public static FollowedMartResponseDto from(MartBookmark bookmark) {
        FollowedMartResponseDto dto = new FollowedMartResponseDto();
        MartShop martShop = bookmark.getMartShop();

        dto.setBookmarkId(bookmark.getBookmarkId());
        dto.setMartshopId(martShop.getMartShopId());
        dto.setMartname(martShop.getName());
        dto.setVisitorCount(martShop.getVisitor());
        //dto.setSalesIndex(calculateSalesIndex(martShop));
        dto.setMartcategory(martShop.getMartCategories().stream()
                .map(MartCategory::getCategoryName)
                .collect(Collectors.toList()));

        return dto;
    }
}
