package com.ll.TeamProject.domain.user.service;

import com.ll.TeamProject.domain.user.dto.DormantAccountProjection;
import com.ll.TeamProject.domain.user.entity.SiteUser;
import com.ll.TeamProject.domain.user.repository.AuthenticationRepository;
import com.ll.TeamProject.global.mail.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDormantService {

    private final AuthenticationRepository authenticationRepository;
    private final EmailService emailService;
    private final UserService userService;

    @Transactional
    public void processDormant() {
        // 기준 날짜 설정 (매월 1일 실행)
        YearMonth currentMonth = YearMonth.now();

        // 각 대상의 범위 조회 및 처리
        List<DormantAccountProjection> notifyCandidates = findCandidatesByMonthsAgo(11);
        List<DormantAccountProjection> lockCandidates = findCandidatesByMonthsAgo(12);
        List<DormantAccountProjection> deleteCandidates = findCandidatesByMonthsAgo(18);

        // 1. 휴면 안내 메일 전송
        sendDormantNotificationEmail(notifyCandidates, currentMonth.plusMonths(1));

        // 2. 계정 잠금 처리
        processUserAction(lockCandidates, SiteUser::lockAccount);

        // 3. 계정 소프트 삭제 처리
        processUserAction(deleteCandidates, SiteUser::delete);
    }

    private List<DormantAccountProjection> findCandidatesByMonthsAgo(int monthsAgo) {
        YearMonth targetMonth = YearMonth.now().minusMonths(monthsAgo);
        LocalDateTime startDate = targetMonth.atDay(1).atStartOfDay();
        LocalDateTime endDate = targetMonth.atEndOfMonth().atTime(LocalTime.MAX);

        return authenticationRepository.findDormantAccountsInDateRange(startDate, endDate);
    }

    private void sendDormantNotificationEmail(List<DormantAccountProjection> candidates, YearMonth nextMonth) {
        LocalDate nextMonthDate = nextMonth.atDay(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일");

        candidates.forEach(candidate -> {
            String message = """
                    장기 미사용 이용자로 %s님 계정이 %s 휴면계정으로 전환될 예정입니다.
                    """.formatted(candidate.getNickname(), nextMonthDate.format(formatter));
            emailService.sendEmail(candidate.getEmail(), "2ndProject 휴면계정 전환 안내", message);
        });
    }

    private void processUserAction(List<DormantAccountProjection> candidates, java.util.function.Consumer<SiteUser> action) {
        candidates.forEach(candidate -> {
            userService.findById(candidate.getId()).ifPresent(action);
        });
    }
}
