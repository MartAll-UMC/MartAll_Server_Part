package com.backend.martall.domain.mart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.backend.martall.domain.mart.entity.MartShop;
@Repository
public interface MartRepository extends JpaRepository<MartShop, Long>{
}
