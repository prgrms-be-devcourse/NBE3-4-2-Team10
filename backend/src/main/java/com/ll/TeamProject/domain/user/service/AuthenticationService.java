package com.ll.TeamProject.domain.user.service;

import com.ll.TeamProject.domain.user.entity.Authentication;
import com.ll.TeamProject.domain.user.entity.SiteUser;
import com.ll.TeamProject.domain.user.repository.AuthenticationRepository;
import com.ll.TeamProject.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationRepository authenticationRepository;
    private final UserRepository userRepository;

    // 최근 로그인 시간 및 로그인 실패 초기화
    public void modifyLastLogin(SiteUser user) {
        authenticationRepository.findByUserId(user.getId())
                .ifPresent(authentication -> {
                    authentication.setLastLogin();  // 최근 로그인 시간 설정
                    authentication.resetFailedAttempts();              // 실패 횟수 초기화
                    authenticationRepository.save(authentication);     // 변경 내용 저장
                });
    }

    // 로그인 실패
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleLoginFailure(SiteUser user) {
        authenticationRepository.findByUserId(user.getId()).ifPresent(authentication -> {

            // 현재 실패 횟수
            int currentFailedAttempts = authentication.getFailedAttempts() + 1;

            // 횟수 반영
            int updatedFailedAttempts = authentication.failedLogin(currentFailedAttempts);

            // 5회 이상 계정 잠김
            if (updatedFailedAttempts >= 5) user.lockAccount();

            authenticationRepository.save(authentication);
            userRepository.save(user);
        });
    }

    public Optional<Authentication> findByUserId(Long id) {
        return authenticationRepository.findByUserId(id);
    }
}
