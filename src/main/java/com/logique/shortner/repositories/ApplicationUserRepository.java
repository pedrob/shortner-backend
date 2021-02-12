package com.logique.shortner.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.logique.shortner.models.ApplicationUser;

public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {
    ApplicationUser findByUsername(String username);
}