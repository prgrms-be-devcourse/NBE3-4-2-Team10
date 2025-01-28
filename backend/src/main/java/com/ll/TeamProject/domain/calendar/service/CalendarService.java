package com.ll.TeamProject.domain.calendar.service;

import com.ll.TeamProject.domain.calendar.entity.Calendar;
import com.ll.TeamProject.domain.calendar.repository.CalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarRepository calendarRepository;

    // 모든 캘린더 조회
    public List<Calendar> getAllCalendars() {
        return calendarRepository.findAll();
    }

    // 특정 캘린더 조회
    public Calendar getCalendarById(Long id) {
        Optional<Calendar> optionalCalendar = calendarRepository.findById(id);
        return optionalCalendar.orElse(null); // 없으면 null 반환
    }

    // 캘린더 삭제
    public boolean deleteCalendar(Long id) {
        if (!calendarRepository.existsById(id)) {
            return false; // 존재하지 않으면 false 반환
        }
        calendarRepository.deleteById(id);
        return true; // 삭제 성공
    }
}