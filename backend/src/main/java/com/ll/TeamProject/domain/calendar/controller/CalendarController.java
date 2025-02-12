package com.ll.TeamProject.domain.calendar.controller;

import com.ll.TeamProject.domain.calendar.dto.CalendarCreateDto;
import com.ll.TeamProject.domain.calendar.dto.CalendarUpdateDto;
import com.ll.TeamProject.domain.calendar.entity.Calendar;
import com.ll.TeamProject.domain.calendar.service.CalendarService;
import com.ll.TeamProject.domain.user.entity.SiteUser;
import com.ll.TeamProject.global.userContext.UserContext;
import com.ll.TeamProject.global.exceptions.ServiceException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/calendars")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@SecurityRequirement(name = "bearerAuth")
public class CalendarController {

    private final CalendarService calendarService;
    private final UserContext userContext;

    private SiteUser getAuthenticatedUser() {
        return userContext.findActor().orElseThrow(() -> new ServiceException("401-1", "로그인을 먼저 해주세요!"));
    }

    @PostMapping
    public ResponseEntity<Calendar> createCalendar(@RequestBody CalendarCreateDto dto) {
        SiteUser user = getAuthenticatedUser();
        dto.setUserId(user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(calendarService.createCalendar(dto));
    }

    @GetMapping
    public ResponseEntity<List<Calendar>> getAllCalendars() {
        SiteUser user = getAuthenticatedUser();
        return ResponseEntity.ok(calendarService.getAllCalendars(user.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCalendarById(@PathVariable Long id) {
        SiteUser user = getAuthenticatedUser();
        Calendar calendar = calendarService.getCalendarById(id);

        if (!calendar.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("해당 캘린더는 열 수 없어요!");
        }
        return ResponseEntity.ok(calendar);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCalendar(@PathVariable Long id, @RequestBody CalendarUpdateDto dto) {
        SiteUser user = getAuthenticatedUser();
        Calendar calendar = calendarService.getCalendarById(id);

        if (!calendar.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("해당 캘린더를 수정할 수 없어요!");
        }
        return ResponseEntity.ok(calendarService.updateCalendar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCalendar(@PathVariable Long id) {
        SiteUser user = getAuthenticatedUser();
        Calendar calendar = calendarService.getCalendarById(id);

        if (!calendar.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("해당 캘린더를 삭제할 수 없어요!");
        }
        calendarService.deleteCalendar(id);
        return ResponseEntity.ok("캘린더가 삭제되었습니다!");
    }
}
