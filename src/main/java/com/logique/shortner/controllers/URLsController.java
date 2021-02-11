package com.logique.shortner.controllers;

import com.logique.shortner.exceptions.ResourceNotFoundException;
import com.logique.shortner.models.URLs;
import com.logique.shortner.repositories.URLsRepository;
import com.logique.shortner.utils.HashGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import java.util.*;

@CrossOrigin(origins = "*")
@RestController
public class URLsController {

    @Autowired
    private URLsRepository urlsRepository;

    @GetMapping("/urls")
    public Page<URLs> getURLs(Pageable pageable) {
        // filter for user id
        return urlsRepository.findAll(pageable);
    }

    @PostMapping("/urls")
    public URLs createURLs(@RequestBody Map<String, String> payload) {
        String originalUrl = payload.get("originalURL");
        Date createdAt = new Date();
        String hash = new HashGenerator().generate();
        URLs urls = new URLs();
        urls.setHash(hash);
        urls.setOriginalURL(originalUrl);
        urls.setCreatedAt(createdAt);
        return urlsRepository.save(urls);
    }

    @GetMapping("/{hash}")
    public RedirectView redirectToOriginal(@PathVariable String hash) {
        String url = urlsRepository.findById(hash).map(URLs::getOriginalURL).orElse(null);
        if (url == null) throw new ResourceNotFoundException("URL not found");
        return new RedirectView(url);
    }
}