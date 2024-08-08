package com.backend.martall.domain.inquiry.repository;


import com.backend.martall.domain.inquiry.entity.InquiryContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InquiryContentRepository extends JpaRepository<InquiryContent, Long> {
}
