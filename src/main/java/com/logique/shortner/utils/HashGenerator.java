
package com.logique.shortner.utils;

import org.apache.commons.lang3.RandomStringUtils;

import org.springframework.stereotype.Service;
@Service
public class HashGenerator {

    public String generate() {
        return RandomStringUtils.random(6, true, true);
    }       

}