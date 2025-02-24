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

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarRepository calendarRepository;
    private final UserRepository userRepository;

    private static final String USER_NOT_FOUND = "사용자를 찾을 수 없습니다.";
    private static final String CALENDAR_NOT_FOUND = "캘린더를 찾을 수 없습니다.";

    //캘린더 생성
    public Calendar createCalendar(CalendarCreateDto dto) {
        SiteUser user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ServiceException("404", USER_NOT_FOUND));

        Calendar calendar = new Calendar(user, dto.getName(), dto.getDescription());
        Calendar savedCalendar = calendarRepository.save(calendar);

        log.info("캘린더 생성 완료 - ID: {}, Name: {}", savedCalendar.getId(), savedCalendar.getName());
        return savedCalendar;
    }

    //특정 유저의 모든 캘린더 조회
    public List<Calendar> getAllCalendars(Long userId) {
        SiteUser user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException("404", USER_NOT_FOUND));

        return calendarRepository.findByUserId(user.getId());
    }


    //특정 캘린더 조회
    public Calendar getCalendarById(Long id) {
        return calendarRepository.findById(id)
                .orElseThrow(() -> new ServiceException("404", CALENDAR_NOT_FOUND));
    }

    //캘린더 수정
    public Calendar updateCalendar(Long id, CalendarUpdateDto dto) {
        Calendar calendar = getCalendarById(id);
        calendar.update(dto);

        log.info("캘린더 수정 완료 - ID: {}, New Name: {}, New Description: {}", id, dto.getName(), dto.getDescription());

        return calendarRepository.save(calendar);
    }

    //캘린더 삭제
    public void deleteCalendar(Long id) {
        calendarRepository.deleteById(id);
        log.info("캘린더 삭제 완료 - ID: {}", id);
    }
}