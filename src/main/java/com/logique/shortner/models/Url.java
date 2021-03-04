package com.logique.shortner.models;

import javax.persistence.*;
import java.util.Date;
@Entity
public class Url {
    
    @Id
    private String hash;

    private String originalURL;

    private Date createdAt;

    private String username;
    
    public Url() {
        
    } 

    public Url(String hash, String originalURL, Date createdAt, String username) {
        this.hash = hash;
        this.originalURL = originalURL;
        this.createdAt = createdAt;
        this.username = username;
    }


    public String getHash() {
        return this.hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getOriginalURL() {
        return this.originalURL;
    }

    public void setOriginalURL(String originalURL) {
        this.originalURL = originalURL;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
