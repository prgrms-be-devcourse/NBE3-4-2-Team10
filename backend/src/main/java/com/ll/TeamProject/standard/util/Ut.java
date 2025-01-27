package com.ll.TeamProject.standard.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.SneakyThrows;

import javax.crypto.SecretKey;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class Ut {

    public static class str {
        public static boolean isBlank(String str) {
            return str == null || str.trim().isEmpty();
        }
    }

    public static class json {
        private static final ObjectMapper om = new ObjectMapper();

        @SneakyThrows
        public static String toString(Object obj) {
            return om.writeValueAsString(obj);
        }
    }

    public static class jwt {

        public static String toString(String secret, long expireSeconds, Map<String, ? extends Serializable> body) {
            SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes());

            Date issuedAt = new Date();
            Date expiresAt = new Date(issuedAt.getTime() + expireSeconds * 1000);

            String jwt = Jwts.builder()
                    .claims(body)
                    .issuedAt(issuedAt)
                    .expiration(expiresAt)
                    .signWith(secretKey)
                    .compact();

            return jwt;
        }

        public static Map<String, Object> payload(String secret, String accessTokenStr) {
            SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes());

            try{
                return (Map<String, Object>) Jwts
                        .parser()
                        .verifyWith(secretKey)
                        .build()
                        .parse(accessTokenStr)
                        .getPayload();
            } catch (Exception e) {
                return null;
            }
        }
    }
}
