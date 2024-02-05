package com.backend.martall.domain.user.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByProviderId(Long provider_id);
    Optional<User> findByUserIdx(Long user_idx);

}
