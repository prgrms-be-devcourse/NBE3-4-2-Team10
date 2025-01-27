package com.ll.TeamProject.domain.user.service;

import com.ll.TeamProject.domain.user.entity.SiteUser;
import com.ll.TeamProject.domain.user.repository.UserRepository;
import com.ll.TeamProject.domain.user.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthTokenService authTokenService;

    public Optional<SiteUser> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public String genAccessToken(SiteUser user) {
        return authTokenService.genAccessToken(user);
    }

    public SiteUser getUserFromAccessToken(String accessToken) {
        Map<String, Object> payload = authTokenService.payload(accessToken);
        if (payload == null) return null;

        long id = (long) payload.get("id");
        String username = (String) payload.get("username");
        Role role = (Role) payload.get("role");

        return new SiteUser(id, username, role);
    }

    public Optional<SiteUser> findByApiKey(String apiKey) {
        return userRepository.findByApiKey(apiKey);
    }
}
