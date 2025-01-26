package com.ll.TeamProject.controller;

import com.ll.TeamProject.domain.calendar.Calendar;
import com.ll.TeamProject.service.CalendarService;
import com.ll.TeamProject.service.dto.CalendarCreateDto;
import com.ll.TeamProject.service.dto.CalendarUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/calendars")
public class CalendarController {

    @Autowired
    private CalendarService calendarService;

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