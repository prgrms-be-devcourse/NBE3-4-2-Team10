package com.ll.TeamProject.domain.calendar.controller;

import com.ll.TeamProject.domain.calendar.entity.Calendar;
import com.ll.TeamProject.domain.calendar.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/calendars")
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarService calendarService;

    // 모든 캘린더 조회
    @GetMapping
    public ResponseEntity<List<Calendar>> getAllCalendars() {
        return ResponseEntity.ok(calendarService.getAllCalendars());
    }

    // 특정 캘린더 조회
    @GetMapping("/{id}")
    public ResponseEntity<Calendar> getCalendarById(@PathVariable Long id) {
        Calendar calendar = calendarService.getCalendarById(id);
        if (calendar == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(calendar);
    }

    // 캘린더 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCalendar(@PathVariable Long id) {
        boolean deleted = calendarService.deleteCalendar(id);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("캘린더를 찾을 수 없습니다.");
        }
        return ResponseEntity.ok("캘린더가 성공적으로 삭제되었습니다.");
    }
}
