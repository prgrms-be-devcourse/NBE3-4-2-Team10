package com.ll.TeamProject.global.baseinit;

import com.ll.TeamProject.domain.user.entity.SiteUser;
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

import static com.ll.TeamProject.domain.user.entity.Role.ADMIN;
import static com.ll.TeamProject.domain.user.entity.Role.USER;

@Configuration
@RequiredArgsConstructor
public class BaseInitData {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
                    .password(passwordEncoder.encode("admin"))
                    .role(ADMIN)
                    .email("admin@ll.com")
                    .apiKey(UUID.randomUUID().toString())
                    .build();
            userRepository.save(admin);

            for (int i = 1; i <= 13; i++) {
                SiteUser user = SiteUser.builder()
                        .username("user" + i)
                        .password(passwordEncoder.encode("password" + i))
                        .role(USER)
                        .email("user" + i + "@ll.com")
                        .apiKey(UUID.randomUUID().toString())
                        .build();
                userRepository.save(user);
            }
        }
    }
}
