package com.ll.TeamProject.domain.calendar.service;

import com.ll.TeamProject.domain.calendar.dto.CalendarCreateDto;
import com.ll.TeamProject.domain.calendar.dto.CalendarUpdateDto;
import com.ll.TeamProject.domain.calendar.entity.Calendar;
import com.ll.TeamProject.domain.calendar.repository.CalendarRepository;
import com.ll.TeamProject.domain.user.entity.SiteUser;
import com.ll.TeamProject.domain.user.repository.UserRepository;
import com.ll.TeamProject.global.exceptions.ServiceException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j // 로깅 추가
@Service
@Transactional
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarRepository calendarRepository;
    private final UserRepository userRepository;

    // 캘린더 생성
    @Transactional
    public Calendar createCalendar(CalendarCreateDto dto) {
        SiteUser user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ServiceException("404", "사용자를 찾을 수 없습니다."));

        Calendar calendar = new Calendar(user, dto.getName(), dto.getDescription());
        Calendar savedCalendar = calendarRepository.save(calendar);

        log.info("캘린더 생성 완료 - ID: {}, Name: {}", savedCalendar.getId(), savedCalendar.getName());

        return savedCalendar;
    }

    // 모든 캘린더 조회 (유저 ID 기반)
    public List<Calendar> getAllCalendars(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ServiceException("404", "사용자를 찾을 수 없습니다.");
        }
        return calendarRepository.findByUserId(userId);
    }

    // 특정 캘린더 조회
    public Calendar getCalendarById(Long id) {
        return calendarRepository.findById(id)
                .orElseThrow(() -> new ServiceException("404", "캘린더를 찾을 수 없습니다."));
    }

    // 캘린더 수정
    @Transactional
    public Calendar updateCalendar(Long id, CalendarUpdateDto dto) {
        Calendar calendar = getCalendarById(id);

        // null 체크 후 업데이트 (기존 값 유지)
        String updatedName = (dto.getName() != null) ? dto.getName() : calendar.getName();
        String updatedDescription = (dto.getDescription() != null) ? dto.getDescription() : calendar.getDescription();

        calendar.update(updatedName, updatedDescription);

        log.info("캘린더 수정 완료 - ID: {}, New Name: {}, New Description: {}", id, updatedName, updatedDescription);

        return calendarRepository.save(calendar);
    }

    // 캘린더 삭제
    @Transactional
    public void deleteCalendar(Long id) {
        Calendar calendar = getCalendarById(id); // 예외 발생 시 자동 처리됨
        calendarRepository.delete(calendar);

        log.info("캘린더 삭제 완료 - ID: {}", id);
    }
}