package com.ll.TeamProject.domain.user.service;

import com.ll.TeamProject.domain.user.entity.Authentication;
import com.ll.TeamProject.domain.user.entity.SiteUser;
import com.ll.TeamProject.domain.user.repository.AuthenticationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationRepository authenticationRepository;

    // 최근 로그인 시간 수정
    public void modifyLastLogin(SiteUser user) {
        authenticationRepository.findByUserId(user.getId()).ifPresent(authentication -> {
            authentication.setLastLogin(LocalDateTime.now());
        });
    }

    // 로그인 실패
    public void handleLoginFailure(SiteUser user) {
        authenticationRepository.findByUserId(user.getId()).ifPresent(authentication -> {

            int attempts = authentication.getFailedAttempts() + 1;

            authentication.failedLogin(attempts);

            authenticationRepository.save(authentication);
        });
    }

    public Optional<Authentication> findByUserId(Long id) {
        return authenticationRepository.findByUserId(id);
    }
}
