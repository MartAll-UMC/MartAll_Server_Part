package com.backend.martall.domain.inquiry.repository;

import com.backend.martall.domain.inquiry.entity.Inquiry;
import com.backend.martall.domain.mart.entity.MartShop;
import com.backend.martall.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    @Query("SELECT i FROM Inquiry i JOIN FETCH i.martShop JOIN FETCH i.user WHERE i.id = :inquiryId")
    Optional<Inquiry> findByInquiryId(Long inquiryId);


    @Query("SELECT i FROM Inquiry i JOIN FETCH i.inquiryContentList JOIN FETCH i.martShop WHERE i.user = :user")
    List<Inquiry> findByUserWithInquiryContentList(User user);

    @Query("SELECT i FROM Inquiry i JOIN FETCH i.inquiryContentList JOIN FETCH i.user WHERE i.martShop = :martShop")
    List<Inquiry> findByMartShopWithInquiryContentList(MartShop martShop);

    @Query("SELECT i FROM Inquiry i JOIN FETCH i.martShop JOIN FETCH i.user JOIN FETCH i.inquiryContentList WHERE i.id = :inquiryId")
    Optional<Inquiry> findByInquiryIdWithContent(Long inquiryId);
}
