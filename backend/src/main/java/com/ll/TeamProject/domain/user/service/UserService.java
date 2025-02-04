package com.ll.TeamProject.domain.user.service;

import com.ll.TeamProject.domain.user.entity.Authentication;
import com.ll.TeamProject.domain.user.entity.SiteUser;
import com.ll.TeamProject.domain.user.enums.AuthType;
import com.ll.TeamProject.domain.user.enums.Role;
import com.ll.TeamProject.domain.user.repository.AuthenticationRepository;
import com.ll.TeamProject.domain.user.repository.UserRepository;
import com.ll.TeamProject.global.exceptions.ServiceException;
import com.ll.TeamProject.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private final Rq rq;

    // username으로 찾기
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
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);

        if (searchKeyword.isBlank()) return findUsersNoKeyword(page, pageSize); // 키워드 없는 유저 목록

        searchKeyword = "%" + searchKeyword + "%"; // 일부만 입력해도 조회되도록

        return switch (searchKeywordType) {
            case "email" -> userRepository.findByRoleAndEmailLike(Role.USER, searchKeyword, pageRequest);
            default -> userRepository.findByRoleAndUsernameLike(Role.USER, searchKeyword, pageRequest);
        };
    }

    // 키워드 없는 user 목록(관리자 제외)
    public Page<SiteUser> findUsersNoKeyword(int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);

        return userRepository.findByRoleNot(Role.ADMIN, pageRequest);
    }

    // JWT 생성
    public String genAccessToken(SiteUser user) {
        return authTokenService.genAccessToken(user);
    }

    // apiKey + JWT 토큰 생성
    public String genAuthToken(SiteUser user) {
        return user.getApiKey() + " " + genAccessToken(user);
    }

    // JWT로 user 찾기
    public SiteUser getUserFromAccessToken(String accessToken) {
        // paylod로 JWT 파싱
        Map<String, Object> payload = authTokenService.payload(accessToken);

        if (payload == null) return null;

        long id = (long) payload.get("id");
        String username = (String) payload.get("username");
        Role role = (Role) payload.get("role");

        // JWT로 얻은 가짜 user 객체(DB에서 조회한 user 아님)
        return new SiteUser(id, username, role);
    }

    // 소셜 로그인 user가 이미 있으면 modify, user가 없으면 회원가입
    public SiteUser modifyOrJoin(String username, String nickname, String email, String providerTypeCode) {
        Optional<SiteUser> opUser = findByUsername(username);

        if (opUser.isPresent()) {
            SiteUser user = opUser.get();
            modify(user);
            return user;
        }

        return join(username, nickname, "", email, providerTypeCode);
    }

    // user 수정 부분 미구현
    public void modify(SiteUser user) {

    }

    // user 가입
    public SiteUser join(String username, String nickname, String password, String email, String providerTypeCode) {
        // 새로운 user 생성
        SiteUser user = SiteUser.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .email(email)
                .role(USER)
                .apiKey(UUID.randomUUID().toString())
                .build();

        user = userRepository.save(user);

        // 소셜 로그인 구분
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

    public SiteUser delete(long id) {
        // 회원 존재 확인
        Optional<SiteUser> userOptional = findById(id);
        if(userOptional.isEmpty()) {
            throw new ServiceException("401-1", "존재하지 않는 사용자입니다.");
        }
        SiteUser userToDelete = userOptional.get();

        // 요청 사용자 확인
        SiteUser actor = rq.getActor();

        // 권한 확인
        if(!userToDelete.getId().equals(actor.getId())) {
            throw new ServiceException("403-1", "접근 권한이 없습니다.");
        }

        // 삭제 로직
        userToDelete.changeNickname("탈퇴한 사용자");
        userToDelete.deleteUsernameEmail();
        userToDelete.delete(true);
        userRepository.save(userToDelete);

        return userToDelete;
    }
}
