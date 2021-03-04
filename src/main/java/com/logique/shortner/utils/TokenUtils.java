
package com.logique.shortner.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import static com.logique.shortner.security.SecurityConstants.TOKEN_PREFIX;
import static com.logique.shortner.security.SecurityConstants.EXPIRATION_TIME;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;


import java.util.Date;
@Service
public class TokenUtils {

    @Value("{jwt.SECRET}")
    private String secret;

    public String getUsernameFromToken(String token) {
        String username = JWT.require(Algorithm.HMAC512(secret.getBytes()))
                    .build()
                    .verify(token.replace(TOKEN_PREFIX, ""))
                    .getSubject();
        return username;
    }

    public String createToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(secret.getBytes()));
    }

}