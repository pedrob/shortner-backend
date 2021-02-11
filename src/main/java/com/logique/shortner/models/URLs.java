package com.logique.shortner.models;

import javax.persistence.*;
import java.util.Date;

@Entity
public class URLs {
    
    @Id
    private String hash;

    private String originalURL;

    private Date createdAt;


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

}
