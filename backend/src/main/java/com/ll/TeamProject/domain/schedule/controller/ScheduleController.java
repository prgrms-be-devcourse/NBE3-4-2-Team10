package com.ll.TeamProject.domain.schedule.controller;

import com.ll.TeamProject.domain.schedule.dto.ScheduleRequestDto;
import com.ll.TeamProject.domain.schedule.dto.ScheduleResponseDto;
import com.ll.TeamProject.domain.schedule.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    //일정생성
    @PostMapping("/{calendarId}")
    public ResponseEntity<ScheduleResponseDto> createSchedule(@PathVariable Long calendarId, @RequestBody @Valid ScheduleRequestDto scheduleRequestDto){
        ScheduleResponseDto responseDto=scheduleService.createSchedule(calendarId,scheduleRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    //일정수정
    @PutMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(@PathVariable Long scheduleId, @RequestBody @Valid ScheduleRequestDto scheduleRequestDto){
        ScheduleResponseDto responseDto=scheduleService.updateSchedule(scheduleId, scheduleRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    //일정삭제
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long scheduleId){
        scheduleService.deleteSchedule(scheduleId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> getSchedules(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        // 날짜 범위로 일정 조회
        List<ScheduleResponseDto> schedules = scheduleService.getSchedules(startDate, endDate);
        return ResponseEntity.ok(schedules);
    }


    //특정 일정 조회
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> getSchedule(@PathVariable Long id){
        ScheduleResponseDto responseDto = scheduleService.getScheduleById(id);
        return ResponseEntity.ok(responseDto);
    }
}
