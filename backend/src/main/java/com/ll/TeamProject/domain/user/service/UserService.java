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
import com.ll.TeamProject.global.mail.EmailService;
import com.ll.TeamProject.global.userContext.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
    private final EmailService emailService;

    private final StringRedisTemplate redisTemplate;

    public LoginDto login(String username, String password) {
        SiteUser user = findByUsername(username)
                .orElseThrow(() -> new ServiceException("401-1", "존재하지 않는 사용자입니다."));

        if (user.isLocked()) throw new ServiceException("403-2", "계정이 잠겨있습니다.");

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

    public void processVerification(String username, String email) {
        SiteUser user = validateUsernameAndEmail(username, email);

        String code = generateVerificationCode();

        redisTemplate.opsForValue().set("username", code, 180, TimeUnit.SECONDS);

        sendVerificationEmail(user.getNickname(), user.getEmail(), code);
    }

    public void verifyAndUnlockAccount(String username, String verificationCode) {
        SiteUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ServiceException("401-1", "존재하지 않는 사용자입니다."));

        if (!isVerificationCodeValid(user, verificationCode)) {
            throw new ServiceException("401-3", "인증번호가 유효하지 않거나 만료되었습니다.");
        }

        redisTemplate.delete("username");
        unlockAccount(user);
    }

    private SiteUser validateUsernameAndEmail(String username, String email) {
        return userRepository.findByUsername(username)
                .map(user -> {
                    if (!user.getEmail().equals(email)) {
                        throw new ServiceException("401-2", "이메일이 일치하지 않습니다.");
                    }
                    return user;
                })
                .orElseThrow(() -> new ServiceException("401-1", "존재하지 않는 사용자입니다."));
    }

    private String generateVerificationCode() {
        return UUID.randomUUID().toString().substring(0, 6);
    }

    private void sendVerificationEmail(String nickname, String email, String verificationCode) {
        String subject = "계정 인증번호";
        String content = String.format("안녕하세요, %s님.\n\n인증번호: %s\n인증번호는 3분 동안 유효합니다.", nickname, verificationCode);

        emailService.sendEmail(email, subject, content);
    }

    private void unlockAccount(SiteUser user) {
        user.unlockAccount();  // 계정 상태 변경 (잠금 해제)
        userRepository.save(user);
    }

    private boolean isVerificationCodeValid(SiteUser user, String verificationCode) {
        String code = redisTemplate.opsForValue().get("username");
        return code.equals(verificationCode);  // 임시 검증 로직
    }

    // username으로 찾기
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

    // apiKey로 user 찾기
    public Optional<SiteUser> findByApiKey(String apiKey) {
        return userRepository.findByApiKey(apiKey);
    }

    // id로 user 찾기
    public Optional<SiteUser> findById(long id) {
        return userRepository.findById(id);
    }

    // 관리자 user 조회
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
        return findByUsername(username)
                .orElseGet(() -> join(username, "", email, providerTypeCode));
    }

    public SiteUser join(String username, String password, String email, String providerTypeCode) {
        SiteUser user = SiteUser.builder()
                .username(username)
                .password(password)
                .nickname(username) // nickname = username 초기 설정
                .email(email)
                .role(USER)
                .apiKey(UUID.randomUUID().toString())
                .locked(false)
                .build();
        user = userRepository.save(user);

        AuthType authType = AuthType.valueOf(providerTypeCode);
        Authentication authentication = Authentication
                .builder()
                .userId(user.getId())
                .authType(authType)
                .failedAttempts(0)
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