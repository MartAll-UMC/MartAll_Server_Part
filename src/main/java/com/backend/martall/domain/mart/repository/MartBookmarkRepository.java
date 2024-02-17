package com.backend.martall.domain.mart.repository;

import com.backend.martall.domain.mart.entity.MartBookmark;
import com.backend.martall.domain.mart.entity.MartShop;
import com.backend.martall.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MartBookmarkRepository extends JpaRepository<MartBookmark, Long> {

    // 사용자별 단골 마트 조회
    List<MartBookmark> findByUser(User user);

    // 특정 사용자와 마트에 대한 단골 여부 확인
    boolean existsByUserAndMartShop(User user, MartShop martShop);

    // 특정 사용자와 마트에 대한 단골 정보 조회
    Optional<MartBookmark> findByUserAndMartShop(User user, MartShop martShop);
}
