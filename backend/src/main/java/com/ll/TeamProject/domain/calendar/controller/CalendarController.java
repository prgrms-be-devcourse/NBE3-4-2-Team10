package com.ll.TeamProject.domain.calendar.controller;

import com.ll.TeamProject.domain.calendar.dto.CalendarCreateDto;
import com.ll.TeamProject.domain.calendar.dto.CalendarUpdateDto;
import com.ll.TeamProject.domain.calendar.entity.Calendar;
import com.ll.TeamProject.domain.calendar.service.CalendarService;
import com.ll.TeamProject.domain.user.entity.SiteUser;
import com.ll.TeamProject.global.exceptions.ServiceException;
import com.ll.TeamProject.global.rq.Rq;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/calendars")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000") // Next.js 프론트엔드와 연결
@SecurityRequirement(name = "bearerAuth")
public class CalendarController {

    private final CalendarService calendarService;
    private final Rq rq;

    //캘린더 생성
    private void validateLogin() {
        SiteUser user = rq.getActor();
        if (user == null) {
            throw new ServiceException("401-1", "로그인이 필요한 서비스입니다.");
        }
    }
    // 캘린더 생성
    @PostMapping
    public ResponseEntity<?> createCalendar(@RequestBody CalendarCreateDto dto) {
        try {
            validateLogin();
            SiteUser user = rq.findByActor().get();
            dto.setUserId(user.getId());

            Calendar calendar = calendarService.createCalendar(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(calendar);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("캘린더 생성 실패: " + e.getMessage());
        }
    }

    //모든 캘린더 조회 (사용자의 모든 캘린더 조회)
    @GetMapping
    public ResponseEntity<List<Calendar>> getAllCalendars() {
        validateLogin();  // 로그인 검증 추가
        SiteUser user = rq.getActor();
        List<Calendar> calendars = calendarService.getAllCalendars(user.getId());
        return ResponseEntity.ok(calendars);
    }

    //특정 캘린더 조회 (사용자의 특정 캘린더 조회)
    @GetMapping("/{id}")
    public ResponseEntity<?> getCalendarById(@PathVariable Long id) {
        validateLogin();
        SiteUser user = rq.getActor(); // 현재 사용자 정보 가져오기
        Calendar calendar = calendarService.getCalendarById(id); //Optional이 아니라 Calendar로 직접 받기

        if (!calendar.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("해당 캘린더에 대한 접근 권한이 없습니다.");
        }

        return ResponseEntity.ok(calendar);
    }

    //캘린더 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCalendar(@PathVariable Long id, @RequestBody CalendarUpdateDto dto) {
        validateLogin();  // 로그인 검증 추가
        SiteUser user = rq.getActor();
        Calendar calendar = calendarService.getCalendarById(id); //Optional 제거하고 직접 Calendar 받기

        if (!calendar.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("해당 캘린더를 수정할 권한이 없습니다.");
        }

        Calendar updatedCalendar = calendarService.updateCalendar(id, dto);
        return ResponseEntity.ok(updatedCalendar);
    }

    //캘린더 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCalendar(@PathVariable Long id) {
        validateLogin();  // 로그인 검증 추가
        SiteUser user = rq.getActor();
        Calendar calendar = calendarService.getCalendarById(id); //Optional 제거하고 직접 Calendar 받기

        if (!calendar.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("해당 캘린더를 삭제할 권한이 없습니다.");
        }

        calendarService.deleteCalendar(id);
        return ResponseEntity.ok("캘린더가 성공적으로 삭제되었습니다.");
    }
}
