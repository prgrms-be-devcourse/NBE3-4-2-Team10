package com.ll.TeamProject.domain.user.service;

import com.ll.TeamProject.domain.user.entity.SiteUser;
import com.ll.TeamProject.domain.user.repository.UserRepository;
import com.ll.TeamProject.domain.user.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    public Page<SiteUser> findUsers(
                String searchKeywordType,
                String searchKeyword,
                int page,
                int pageSize
    ) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);

        if (searchKeyword.isBlank()) return findUsersNoKeyword(page, pageSize); // 키원드 없는 유저 목록

        searchKeyword = "%" + searchKeyword + "%"; // 일부만 입력해도 조회되도록

        return switch (searchKeywordType) {
            case "email" -> userRepository.findByRoleAndEmailLike(Role.USER, searchKeyword, pageRequest);
            default -> userRepository.findByRoleAndUsernameLike(Role.USER, searchKeyword, pageRequest);
        };
    }

    // 키워드 없는 유저 목록(관리자 제외)
    public Page<SiteUser> findUsersNoKeyword(int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);

        return userRepository.findByRoleNot(Role.ADMIN, pageRequest);
    }
}
