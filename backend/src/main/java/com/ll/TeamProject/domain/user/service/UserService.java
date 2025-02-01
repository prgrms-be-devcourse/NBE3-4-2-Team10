package com.ll.TeamProject.domain.user.service;

import com.ll.TeamProject.domain.user.entity.Authentication;
import com.ll.TeamProject.domain.user.entity.SiteUser;
import com.ll.TeamProject.domain.user.enums.Role;
import com.ll.TeamProject.domain.user.repository.AuthenticationRepository;
import com.ll.TeamProject.domain.user.repository.UserRepository;
import com.ll.TeamProject.global.exceptions.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.ll.TeamProject.domain.user.enums.AuthType.KAKAO;
import static com.ll.TeamProject.domain.user.enums.Role.USER;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthTokenService authTokenService;
    private final AuthenticationRepository authenticationRepository;

    public Optional<SiteUser> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public String genAccessToken(SiteUser user) {
        return authTokenService.genAccessToken(user);
    }

    public String genAuthToken(SiteUser user) {
        return user.getApiKey() + " " + genAccessToken(user);
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

    public Optional<SiteUser> findById(long id) {
        return userRepository.findById(id);
    }

    public Page<SiteUser> findUsers(
                String searchKeywordType,
                String searchKeyword,
                int page,
                int pageSize
    ) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);

        if (searchKeyword.isBlank()) return findUsersNoKeyword(page, pageSize); // 키워드 없는 유저 목록

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

    public SiteUser modifyOrJoin(String username, String nickname, String email) {
        Optional<SiteUser> opUser = findByUsername(username);

        if (opUser.isPresent()) {
            SiteUser user = opUser.get();
            modify(user);
            return user;
        }

        return join(username, nickname, "", "");
    }

    public void modify(SiteUser user) {

    }

    public SiteUser join(String username, String nickname, String password, String email) {
        userRepository
                .findByUsername(username)
                .ifPresent(user -> {
                    throw new ServiceException("409-1", "해당 username은 이미 사용중입니다.");
                });

        SiteUser user = SiteUser.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .email(email)
                .role(USER)
                .apiKey(UUID.randomUUID().toString())
                .build();

        user = userRepository.save(user);

        Authentication authentication = Authentication
                .builder()
                .userId(user.getId())
                .authType(KAKAO)
                .failedAttempts(0)
                .isLocked(false)
                .build();

        authenticationRepository.save(authentication);

        return user;
    }
}
