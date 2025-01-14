package com.mekanapp.mekanuserms.userprofile;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {

    UserProfile findByUserId(UUID userId);

}
