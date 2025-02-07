package com.ll.TeamProject.domain.user.service;

import com.ll.TeamProject.domain.user.dto.LoginDto;
import com.ll.TeamProject.domain.user.dto.UserDto;
import com.ll.TeamProject.domain.user.entity.Authentication;
import com.ll.TeamProject.domain.user.entity.SiteUser;
import com.ll.TeamProject.domain.user.enums.AuthType;
import com.ll.TeamProject.domain.user.enums.Role;
import com.ll.TeamProject.domain.user.repository.AuthenticationRepository;
import com.ll.TeamProject.domain.user.repository.UserRepository;
import com.ll.TeamProject.global.exceptions.ServiceException;
import com.ll.TeamProject.global.userContext.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.ll.TeamProject.domain.user.enums.Role.USER;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthTokenService authTokenService;
    private final AuthenticationRepository authenticationRepository;
    private final UserContext userContext;
    private final AuthenticationService authenticationService;
    private final ApplicationContext applicationContext;
    private final ForbiddenService forbiddenService;

    public LoginDto login(String username, String password) {
        SiteUser user = findByUsername(username)
                .orElseThrow(() -> new ServiceException("401-1", "존재하지 않는 사용자입니다."));

        PasswordEncoder passwordEncoder = applicationContext.getBean(PasswordEncoder.class);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            authenticationService.handleLoginFailure(user);
            throw new ServiceException("401-2", "비밀번호가 일치하지 않습니다.");
        }

        authenticationService.modifyLastLogin(user);

        String accessToken = userContext.makeAuthCookies(user);

        return new LoginDto(
                new UserDto(user),
                user.getApiKey(),
                accessToken
        );
    }

    public void logout(HttpServletRequest request) {
        request.getSession().invalidate();

        userContext.deleteCookie("accessToken");
        userContext.deleteCookie("apiKey");
        userContext.deleteCookie("JSESSIONID");

        SecurityContextHolder.clearContext();
    }

    public Optional<SiteUser> findByUsername(String username) {
        return userRepository.findByUsername(username);
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

        if (page < 1) throw new ServiceException("400-1", "페이지 번호는 1 이상이어야 합니다.");
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        if (searchKeyword.isBlank()) return findUsersNoKeyword(page, pageSize);
        searchKeyword = "%" + searchKeyword + "%";

        return switch (searchKeywordType) {
            case "email" ->
                    userRepository.findByRoleAndEmailLikeAndIsDeletedFalse(Role.USER, searchKeyword, pageRequest);
            default -> userRepository.findByRoleAndUsernameLikeAndIsDeletedFalse(Role.USER, searchKeyword, pageRequest);
        };
    }

    public Page<SiteUser> findUsersNoKeyword(int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        return userRepository.findByRoleNot(Role.ADMIN, pageRequest);
    }

    public String genAccessToken(SiteUser user) {
        return authTokenService.genAccessToken(user);
    }

    public String genAuthToken(SiteUser user) {
        return user.getApiKey() + " " + genAccessToken(user);
    }

    // JWT 로 얻은 가짜 user 객체 (DB 에서 조회한 user 아님)
    public SiteUser getUserFromAccessToken(String accessToken) {
        Map<String, Object> payload = authTokenService.payload(accessToken);

        if (payload == null) return null;

        long id = (long) payload.get("id");
        String username = (String) payload.get("username");
        Role role = (Role) payload.get("role");

        return new SiteUser(id, username, role);
    }

    public SiteUser findOrRegisterUser(String username, String email, String providerTypeCode) {
        Optional<SiteUser> opUser = findByUsername(username);

        if (opUser.isPresent()) {
            SiteUser user = opUser.get();
            return user;
        }

        return join(username, "", email, providerTypeCode);
    }

    public SiteUser join(String username, String password, String email, String providerTypeCode) {
        SiteUser user = SiteUser.builder()
                .username(username)
                .password(password)
                .nickname(username) // nickname = username 초기 설정
                .email(email)
                .role(USER)
                .apiKey(UUID.randomUUID().toString())
                .build();
        user = userRepository.save(user);

        AuthType authType = AuthType.valueOf(providerTypeCode);
        Authentication authentication = Authentication
                .builder()
                .userId(user.getId())
                .authType(authType)
                .failedAttempts(0)
                .isLocked(false)
                .build();
        authenticationRepository.save(authentication);

        return user;
    }

    public void modify(String nickname) {
        if (forbiddenService.isForbidden(nickname)) {
            throw new ServiceException("400-1", "해당 닉네임은 사용할 수 없습니다.");
        }

        SiteUser actor = userContext.findActor().get();
        try {
            actor.changeNickname(nickname);
            userRepository.save(actor);
        } catch (DataIntegrityViolationException exception) {
            throw new ServiceException("409-1", "이미 사용중인 닉네임입니다.");
        }

        // 수정된 닉네임 바로 적용되도록 쿠키 수정
        userContext.makeAuthCookies(actor);
    }

    public UserDto delete(long id) {
        Optional<SiteUser> userOptional = findById(id);
        if (userOptional.isEmpty()) {
            throw new ServiceException("401-1", "존재하지 않는 사용자입니다.");
        }
        SiteUser userToDelete = userOptional.get();

        validatePermission(userToDelete);

        userToDelete.delete();
        userRepository.save(userToDelete);

        return new UserDto(userToDelete);
    }

    public void validatePermission(SiteUser userToDelete) {
        SiteUser actor = userContext.getActor();
        if (actor.getUsername().equals("admin")) return;

        if (!userToDelete.getUsername().equals(actor.getUsername())) {
            throw new ServiceException("403-1", "접근 권한이 없습니다.");
        }
    }
}