package com.ll.TeamProject.service;

import com.ll.TeamProject.domain.calendar.Calendar;
import com.ll.TeamProject.repository.CalendarRepository;
import com.ll.TeamProject.service.dto.CalendarCreateDto;
import com.ll.TeamProject.service.dto.CalendarUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalendarService {

    @Autowired
    private CalendarRepository calendarRepository;

    // 캘린더 생성
    public Calendar createCalendar(CalendarCreateDto createDto) {
        Calendar calendar = new Calendar();
        calendar.setName(createDto.getTitle());
        calendar.setDescription(createDto.getDescription());
        // 추가적인 로직 필요 (예: 멤버 설정)
        return calendarRepository.save(calendar);
    }

    // 캘린더 수정
    public Calendar updateCalendar(Long calendarId, CalendarUpdateDto updateDto) {
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new IllegalArgumentException("캘린더가 존재하지 않습니다."));
        calendar.setName(updateDto.getTitle());
        calendar.setDescription(updateDto.getDescription());
        // 추가적인 로직 필요 (예: 멤버 수정)
        return calendarRepository.save(calendar);
    }

    // 캘린더 삭제
    public void deleteCalendar(Long calendarId) {
        calendarRepository.deleteById(calendarId);
    }

    // 캘린더 조회
    public Calendar getCalendar(Long calendarId) {
        return calendarRepository.findById(calendarId)
                .orElseThrow(() -> new IllegalArgumentException("캘린더가 존재하지 않습니다."));
    }

    // 캘린더 리스트 조회
    public List<Calendar> getAllCalendars() {
        return calendarRepository.findAll();
    }
}