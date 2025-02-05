package com.ll.TeamProject.domain.calendar.controller;

import com.ll.TeamProject.domain.calendar.dto.CalendarCreateDto;
import com.ll.TeamProject.domain.calendar.dto.CalendarUpdateDto;
import com.ll.TeamProject.domain.calendar.entity.Calendar;
import com.ll.TeamProject.domain.calendar.service.CalendarService;
import com.ll.TeamProject.domain.user.entity.SiteUser;
import com.ll.TeamProject.global.rq.Rq;
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
    private final Rq rq;

    // 캘린더 생성
    @PostMapping
    public ResponseEntity<Calendar> createCalendar(@RequestBody CalendarCreateDto dto) {
        SiteUser user = rq.getActor();
        dto.setUserId(user.getId());
        Calendar calendar = calendarService.createCalendar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(calendar);
    }
    // 모든 캘린더 조회  (사용자의 모든 캘린더 조회)
    @GetMapping
    public ResponseEntity<List<Calendar>> getAllCalendars() {
        SiteUser user = rq.getActor();
        return ResponseEntity.ok(calendarService.getAllCalendars(user.getId()));
    }


    // 특정 캘린더 조회 (사용자의 5개의 캘린더중 3번 캘린더 조회)
    @GetMapping("/{id}")
    public ResponseEntity<Calendar> getCalendarById(@PathVariable Long id) {
        SiteUser user = rq.getActor();
        Calendar calendar = calendarService.getCalendarById(id);

        // 본인의 캘린더가 아닌 경우 접근 거부
        if (calendar == null || !calendar.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        return ResponseEntity.ok(calendar);
    }

    // 캘린더 수정
    @PutMapping("/{id}")
    public ResponseEntity<Calendar> updateCalendar(
            @PathVariable Long id,
            @RequestBody CalendarUpdateDto dto) {
        SiteUser user = rq.getActor();
        Calendar calendar = calendarService.getCalendarById(id);

        // 본인의 캘린더가 아닌 경우 접근 거부
        if (calendar == null || !calendar.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        calendar = calendarService.updateCalendar(id, dto);
        return ResponseEntity.ok(calendar);
    }
    // 캘린더 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCalendar(@PathVariable Long id) {
        SiteUser user = rq.getActor();
        Calendar calendar = calendarService.getCalendarById(id);

        // 본인의 캘린더가 아닌 경우 접근 거부
        if (calendar == null || !calendar.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("접근 권한이 없습니다.");
        }

        boolean deleted = calendarService.deleteCalendar(id);
        return ResponseEntity.ok("캘린더가 성공적으로 삭제되었습니다.");
    }
}
