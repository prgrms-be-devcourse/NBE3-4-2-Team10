package com.ll.TeamProject.global.initData;

import com.ll.TeamProject.domain.calendar.entity.Calendar;
import com.ll.TeamProject.domain.calendar.repository.CalendarRepository;
import com.ll.TeamProject.domain.user.entity.Authentication;
import com.ll.TeamProject.domain.user.entity.ForbiddenNickname;
import com.ll.TeamProject.domain.user.entity.SiteUser;
import com.ll.TeamProject.domain.user.repository.AuthenticationRepository;
import com.ll.TeamProject.domain.user.repository.ForbiddenRepository;
import com.ll.TeamProject.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.UUID;

import static com.ll.TeamProject.domain.user.enums.AuthType.LOCAL;
import static com.ll.TeamProject.domain.user.enums.Role.ADMIN;
import static com.ll.TeamProject.domain.user.enums.Role.USER;

@Configuration
@RequiredArgsConstructor
public class BaseInitData {

    private final CalendarRepository calendarRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationRepository authenticationRepository;
    private final ForbiddenRepository forbiddenRepository;

    @Autowired
    @Lazy
    private BaseInitData self;

    @Bean
    public ApplicationRunner baseInitDataApplicationRunner() {
        return args -> {
            self.work1();
            self.work2();
            self.work3();
        };
    }
    @Transactional
    public void work1() {
        // 관리자 계정 만들기
        if (userRepository.count() == 0) {
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

    @Transactional
    public void work2() {
        if (calendarRepository.count() == 0) {
            // 각 사용자별 생성할 캘린더 수 정의
            Map<String, Integer> userCalendarCounts = Map.of(
                    "user2", 5,
                    "user3", 4,
                    "user4", 2,
                    "user5", 1
            );

            // 각 사용자별로 캘린더 생성
            userCalendarCounts.forEach((username, count) -> {
                SiteUser user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new IllegalStateException(username + "를 찾을 수 없습니다."));

                for (int i = 1; i <= count; i++) {
                    Calendar calendar = new Calendar(
                            user,
                            username + "의 캘린더 " + i,
                            username + "의 " + i + "번째 테스트 캘린더입니다."
                    );
                    calendarRepository.save(calendar);
                }
            });
        }
    }

    @Transactional
    public void work3() {
        // 닉네임 변경 금지어 설정
        if (forbiddenRepository.count() == 0) {

            String[] forbiddenNames = {
                    "어?", "404", "200", "500", "null",
                    "DROP TABLE Site_User", "rm -rf", "undefined",
                    "git push origin main --force", "NullPointerException",
                    "sudo", "localhost", "test", "guest",
                    "admin", "error", "exception", "deprecated",
                    "Kakao", "Google"
            };

            for (String name : forbiddenNames) {
                ForbiddenNickname forbiddenNickname = new ForbiddenNickname(name);
                forbiddenRepository.save(forbiddenNickname);
            }
        }
    }
}
