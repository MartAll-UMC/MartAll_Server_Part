package com.backend.martall.domain.inquiry.repository;

import com.backend.martall.domain.inquiry.entity.InquiryResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface InquiryResponseRepository extends JpaRepository<InquiryResponse, Long> {
    Optional<InquiryResponse> findByInquiryId(Long inquiryId);
}
