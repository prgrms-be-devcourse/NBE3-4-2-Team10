package com.ll.TeamProject.global.initData;

import com.ll.TeamProject.domain.user.entity.Authentication;
import com.ll.TeamProject.domain.user.entity.SiteUser;
import com.ll.TeamProject.domain.user.repository.AuthenticationRepository;
import com.ll.TeamProject.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static com.ll.TeamProject.domain.user.enums.AuthType.LOCAL;
import static com.ll.TeamProject.domain.user.enums.Role.ADMIN;
import static com.ll.TeamProject.domain.user.enums.Role.USER;
import static com.ll.TeamProject.domain.user.entity.Role.ADMIN;
import static com.ll.TeamProject.domain.user.entity.Role.USER;

@Configuration
@RequiredArgsConstructor
public class BaseInitData {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationRepository authenticationRepository;

    @Autowired
    @Lazy
    private BaseInitData self;

    @Bean
    public ApplicationRunner baseInitDataApplicationRunner() {
        return args -> {
            self.work1();
        };
    }

    @Transactional
    public void work1() {
        // 관리자 계정 만들기
        if(userRepository.count() == 0) {
            SiteUser admin = SiteUser
                    .builder()
                    .username("admin")
                    .nickname("관리자")
                    .password(passwordEncoder.encode("admin"))
                    .role(ADMIN)
                    .email("admin@ll.com")
                    .apiKey(UUID.randomUUID().toString())
                    .build();
            admin = userRepository.save(admin);

            Authentication authentication = Authentication
                    .builder()
                    .userId(admin.getId())
                    .authType(LOCAL)
                    .failedAttempts(0)
                    .isLocked(false)
                    .build();

            authenticationRepository.save(authentication);

            for (int i = 1; i <= 13; i++) {
                SiteUser user = SiteUser.builder()
                        .username("user" + i)
                        .nickname("테스트 회원" + i)
                        .password(passwordEncoder.encode("1234"))
                        .role(USER)
                        .email("user" + i + "@test.com")
                        .apiKey(UUID.randomUUID().toString())
                        .build();
                user = userRepository.save(user);

                Authentication userAuthentication = Authentication
                        .builder()
                        .userId(user.getId())
                        .authType(LOCAL)
                        .failedAttempts(0)
                        .isLocked(false)
                        .build();

                authenticationRepository.save(userAuthentication);
            }
        }
    }
}
