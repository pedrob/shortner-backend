package com.logique.shortner.repositories;

import com.logique.shortner.models.Url;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.*;

@Repository
public interface UrlRepository extends JpaRepository<Url, String> {
    
    @Override
    Optional<Url> findById(String id);

    Page<Url> findByUsername(Pageable pageable, String username);
}