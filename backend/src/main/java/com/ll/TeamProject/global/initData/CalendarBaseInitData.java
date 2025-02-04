package com.ll.TeamProject.global.initData;

import com.ll.TeamProject.domain.calendar.entity.Calendar;
import com.ll.TeamProject.domain.calendar.repository.CalendarRepository;
import com.ll.TeamProject.domain.user.entity.SiteUser;
import com.ll.TeamProject.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class CalendarBaseInitData {
    private final CalendarRepository calendarRepository;
    private final UserRepository userRepository;

    @Autowired
    @Lazy
    private CalendarBaseInitData self;

    @Bean
    @DependsOn("baseInitDataApplicationRunner")  // BaseInitData 실행 후 실행
    public ApplicationRunner calendarInitDataApplicationRunner() {
        return args -> {
            self.work1();
        };
    }

    @Transactional
    public void work1() {
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
}