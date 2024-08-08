package com.backend.martall.domain.inquiry.repository;

import com.backend.martall.domain.inquiry.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    @Query("SELECT i FROM Inquiry i JOIN FETCH i.martShop JOIN FETCH i.user WHERE i.id = :inquiryId")
    Optional<Inquiry> findById(Long inquiryId);
}
