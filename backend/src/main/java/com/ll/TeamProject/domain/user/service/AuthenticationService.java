package com.ll.TeamProject.domain.user.service;

import com.ll.TeamProject.domain.user.entity.Authentication;
import com.ll.TeamProject.domain.user.entity.SiteUser;
import com.ll.TeamProject.domain.user.repository.AuthenticationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationRepository authenticationRepository;

    // 최근 로그인 시간 및 로그인 실패 초기화
    public void modifyLastLogin(SiteUser user) {
        authenticationRepository.findByUserId(user.getId())
                .ifPresent(authentication -> {
                    authentication.setLastLogin();
                    authentication.resetFailedAttempts();
                    authenticationRepository.save(authentication);
                });
    }

    // 로그인 실패
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleLoginFailure(SiteUser user) {
        authenticationRepository.findByUserId(user.getId()).ifPresent(authentication -> {

            int currentFailedAttempts = authentication.getFailedAttempts() + 1;
            int updatedFailedAttempts = authentication.failedLogin(currentFailedAttempts);
            if (updatedFailedAttempts >= 5) authentication.lockAccount();
            authenticationRepository.save(authentication);
        });
    }

    public Optional<Authentication> findByUserId(Long id) {
        return authenticationRepository.findByUserId(id);
    }
}
