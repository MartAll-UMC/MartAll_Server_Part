package com.backend.martall.domain.user.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByProviderId(Long provider_id);
    Optional<User> findByUserIdx(Long user_idx);
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.longitude = :longitude, u.latitude = :latitude, u.address = :address WHERE u.userIdx = :user_idx")
    void updateUserLocation(@Param("user_idx") Long userIdx, @Param("longitude") Double longitude, @Param("latitude") Double latitude, @Param("address") String address);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.locationRange = :location_range WHERE u.userIdx = :user_idx")
    void updateLocationRange(@Param("user_idx") Long userIdx, @Param("location_range") Integer locationRange);

    @Query("SELECT u.userIdx FROM User u WHERE u.refreshToken = :refreshToken")
    Long findIdByRefreshToken(@Param("refreshToken") String refreshToken);

    Boolean existsById(String email);
}
