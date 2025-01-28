package com.ll.TeamProject.domain.calendar.controller;

import com.ll.TeamProject.domain.calendar.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class CalendarViewController {

    private final CalendarService calendarService;

    // 캘린더 리스트 페이지 렌더링
    @GetMapping("/calendars")
    public String showCalendarList(Model model) {
        model.addAttribute("calendars", calendarService.getAllCalendars());
        return "calendar"; // templates/calendar.html 파일을 렌더링
    }
}