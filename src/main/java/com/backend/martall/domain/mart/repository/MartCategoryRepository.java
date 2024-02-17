package com.backend.martall.domain.mart.repository;

import com.backend.martall.domain.mart.entity.MartCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MartCategoryRepository extends JpaRepository<MartCategory, Long> {
}
