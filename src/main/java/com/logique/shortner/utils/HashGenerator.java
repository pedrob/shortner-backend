
package com.logique.shortner.utils;

import org.apache.commons.lang3.RandomStringUtils;

public class HashGenerator {

    public String generate() {
        return RandomStringUtils.random(8, true, true);
    }       

}