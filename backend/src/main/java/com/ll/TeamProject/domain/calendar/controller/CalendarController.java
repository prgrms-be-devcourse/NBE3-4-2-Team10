package com.ll.TeamProject.domain.calendar.controller;

import com.ll.TeamProject.domain.calendar.dto.CalendarCreateDto;
import com.ll.TeamProject.domain.calendar.dto.CalendarUpdateDto;
import com.ll.TeamProject.domain.calendar.entity.Calendar;
import com.ll.TeamProject.domain.calendar.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller  // @RestController에서 @Controller로 변경
@RequiredArgsConstructor  // 생성자 주입을 자동으로 처리해줌
public class CalendarController {

    private final CalendarService calendarService;  // 의존성 주입

    // 캘린더 페이지 TEST용?
    @GetMapping("/calendar")
    public String showCalendarPage() {
        return "calendar";
    }

    // 캘린더 생성
    @PostMapping
    public Calendar createCalendar(@RequestBody CalendarCreateDto createDto) {
        return calendarService.createCalendar(createDto);
    }

    // 캘린더 수정
    @PutMapping("/{calendarId}")
    public Calendar updateCalendar(@PathVariable Long calendarId, @RequestBody CalendarUpdateDto updateDto) {
        return calendarService.updateCalendar(calendarId, updateDto);
    }

    // 캘린더 삭제
    @DeleteMapping("/{calendarId}")
    public void deleteCalendar(@PathVariable Long calendarId) {
        calendarService.deleteCalendar(calendarId);
    }

    // 캘린더 조회
    @GetMapping("/{calendarId}")
    public Calendar getCalendar(@PathVariable Long calendarId) {
        return calendarService.getCalendar(calendarId);
    }

    // 캘린더 리스트 조회
    @GetMapping
    public List<Calendar> getAllCalendars() {
        return calendarService.getAllCalendars();
    }
}