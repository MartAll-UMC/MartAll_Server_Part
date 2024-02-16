package com.backend.martall.domain.mart.service;

import com.backend.martall.domain.mart.dto.CategoryCreateRequestDto;
import com.backend.martall.domain.mart.entity.MartCategory;
import com.backend.martall.domain.mart.entity.MartShop;
import com.backend.martall.domain.mart.entity.MartTag;
import com.backend.martall.domain.mart.repository.MartCategoryRepository;
import com.backend.martall.domain.mart.repository.MartRepository;
import com.backend.martall.domain.user.entity.UserRepository;
import com.backend.martall.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.backend.martall.global.exception.ResponseStatus.MART_CATEGORY_NAME_WRONG;
import static com.backend.martall.global.exception.ResponseStatus.MART_NAME_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class MartCategoryService {

    private final MartRepository martRepository;
    private final MartCategoryRepository martCategoryRepository;

    public void createCategory(CategoryCreateRequestDto categoryCreateRequestDto) {

        // 카테고리 이름이 이넘에 있는지 확인
        if(!MartTag.existByName(categoryCreateRequestDto.getCategoryName())||categoryCreateRequestDto.getCategoryName().equals("전체")) {
            throw new BadRequestException(MART_CATEGORY_NAME_WRONG);
        }

        // 마트가 존재하는 마트인지 확인
        MartShop martShop;
        try {
            martShop = martRepository.findById(categoryCreateRequestDto.getMartShopId()).get();
        } catch (RuntimeException e) {
            throw new BadRequestException(MART_NAME_NOT_FOUND);
        }


        MartCategory martCategory = MartCategory.builder()
                .categoryName(categoryCreateRequestDto.getCategoryName())
                .build();

        martShop.addMartCategory(martCategory);

        martCategoryRepository.save(martCategory);
    }
}
