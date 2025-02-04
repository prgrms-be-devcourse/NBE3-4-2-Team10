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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarRepository calendarRepository;
    private final UserRepository userRepository;

    //캘린더 생성
    @Transactional
    public Calendar createCalendar(CalendarCreateDto dto) {
        SiteUser user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ServiceException("404", "사용자를 찾을 수 없습니다."));

        Calendar calendar = new Calendar(user, dto.getName(), dto.getDescription());
        Calendar savedCalendar = calendarRepository.save(calendar);

        //캘린더 저장 후 검증 (예외 처리 보강)
        if (savedCalendar.getId() == null) {
            throw new ServiceException("500", "캘린더 생성에 실패했습니다.");
        }

        return savedCalendar;
    }

    //모든 캘린더 조회 (유저 ID 기반)
    public List<Calendar> getAllCalendars(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ServiceException("404", "사용자를 찾을 수 없습니다.");
        }
        return calendarRepository.findByUserId(userId);
    }

    //특정 캘린더 조회
    public Calendar getCalendarById(Long id) {
        return calendarRepository.findById(id)
                .orElseThrow(() -> new ServiceException("404", "캘린더를 찾을 수 없습니다."));
    }

    //캘린더 수정
    @Transactional
    public Calendar updateCalendar(Long id, CalendarUpdateDto dto) {
        Calendar calendar = getCalendarById(id);

        if (dto.getName() != null) {
            calendar.update(dto.getName(), dto.getDescription());
        }

        return calendarRepository.save(calendar);
    }

    //캘린더 삭제
    @Transactional
    public boolean deleteCalendar(Long id) {
        Calendar calendar = getCalendarById(id);
        if (calendar == null) {
            throw new ServiceException("404", "캘린더를 찾을 수 없습니다.");
        }

        calendarRepository.delete(calendar);
        return !calendarRepository.existsById(id); // 삭제 여부 확인
    }
}