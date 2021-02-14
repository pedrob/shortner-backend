
package com.logique.shortner.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import static com.logique.shortner.security.SecurityConstants.TOKEN_PREFIX;
import org.springframework.stereotype.Service;
@Service
public class TokenUtils {

    public String getUsernameFromToken(String SECRET, String token) {
        String username = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                    .build()
                    .verify(token.replace(TOKEN_PREFIX, ""))
                    .getSubject();
        return username;
    }

}