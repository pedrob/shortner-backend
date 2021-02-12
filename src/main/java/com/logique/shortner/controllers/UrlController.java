package com.logique.shortner.controllers;

import com.logique.shortner.exceptions.ResourceNotFoundException;
import com.logique.shortner.models.ApplicationUser;
import com.logique.shortner.models.Url;
import com.logique.shortner.repositories.UrlRepository;
import com.logique.shortner.repositories.ApplicationUserRepository;
import com.logique.shortner.utils.HashGenerator;
import com.logique.shortner.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.beans.factory.annotation.Value;
import java.util.*;

@CrossOrigin(origins = "*")
@RestController
public class UrlController {

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private ApplicationUserRepository userRepository;

    @GetMapping("/urls")
    public Page<Url> getUrls(@RequestHeader("Authorization") String token, @Value("{jwt.SECRET}") String SECRET, Pageable pageable) {
        String username = new TokenUtils().getUsernameFromToken(SECRET, token);
        return urlRepository.findByUsername(pageable, username);
    }

    @PostMapping("/urls")
    public Url createUrl(@RequestHeader("Authorization") String token, @Value("{jwt.SECRET}") String SECRET, @RequestBody Map<String, String> payload) {
        String username = new TokenUtils().getUsernameFromToken(SECRET, token);
        ApplicationUser user = userRepository.findByUsername(username);
        String originalUrl = payload.get("originalURL");
        Date createdAt = new Date();
        String hash = new HashGenerator().generate();
        Url url = new Url();
        url.setHash(hash);
        url.setOriginalURL(originalUrl);
        url.setCreatedAt(createdAt);
        url.setUsername(user.getUsername());
        return urlRepository.save(url);
    }

    @GetMapping("/su/{hash}")
    public RedirectView redirectToOriginalURL(@PathVariable String hash) {
        String url = urlRepository.findById(hash).map(Url::getOriginalURL).orElse(null);
        if (url == null) throw new ResourceNotFoundException("URL not found");
        return new RedirectView(url);
    }
}