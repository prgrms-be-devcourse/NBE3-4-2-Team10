package com.ll.TeamProject.domain.user.service;

import com.ll.TeamProject.domain.user.entity.SiteUser;
import com.ll.TeamProject.domain.user.entity.Role;
import com.ll.TeamProject.standard.util.Ut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthTokenService {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${accessToken.expirationSeconds}")
    private long accessTokenExpirationSeconds;

    String genAccessToken(SiteUser user) {
        long id = user.getId();
        String username = user.getUsername();
        Role role = user.getRole();

        return Ut.jwt.toString(
                secretKey,
                accessTokenExpirationSeconds,
                Map.of("id", id, "username", username, "role", role)
        );
    }

    public Map<String, Object> payload(String accessToken) {
        Map<String, Object> parsedPayload = Ut.jwt.payload(secretKey, accessToken);

        if (parsedPayload == null) return null;

        long id = (long) (Integer) parsedPayload.get("id");
        String username = (String) parsedPayload.get("username");
        Role role = Role.valueOf((String) parsedPayload.get("role"));

        return Map.of("id", id, "username", username, "role", role);
    }
}
