package com.logique.shortner.controllers;

import com.logique.shortner.exceptions.ResourceNotFoundException;
import com.logique.shortner.models.ApplicationUser;
import com.logique.shortner.models.Url;
import com.logique.shortner.repositories.UrlRepository;
import com.logique.shortner.utils.HashGenerator;
import com.logique.shortner.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import java.util.*;

@CrossOrigin(origins = "*")
@RestController
public class UrlController {

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private HashGenerator hashGenerator;
    
    @Autowired
    private TokenUtils tokenUtils;

    @GetMapping("/urls")
    public Page<Url> getUrls(@RequestHeader("Authorization") String token, Pageable pageable) {
        String username = tokenUtils.getUsernameFromToken(token);
        return urlRepository.findByUsername(pageable, username);
    }

    @PostMapping("/urls")
    public Url createUrl(@RequestHeader("Authorization") String token, @RequestBody Map<String, String> payload) {
        String username = tokenUtils.getUsernameFromToken(token);
        String originalUrl = payload.get("originalURL");
        String hash = hashGenerator.generate();
        Url url = new Url(hash, originalUrl, new Date(), username);
        return urlRepository.save(url);
    }

    @GetMapping("/su/{hash}")
    public RedirectView redirectToOriginalURL(@PathVariable String hash) {
        String url = urlRepository.findById(hash).map(Url::getOriginalURL).orElse(null);
        if (url == null) throw new ResourceNotFoundException("URL não encontrada");
        return new RedirectView(url);
    }
}