package com.ll.TeamProject.domain.schedule.controller;

import com.ll.TeamProject.domain.schedule.dto.ScheduleRequestDto;
import com.ll.TeamProject.domain.schedule.dto.ScheduleResponseDto;
import com.ll.TeamProject.domain.schedule.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calendars/{calendarId}/schedules")  // 모든 API에 calendarId 추가
public class ScheduleController {

    private final ScheduleService scheduleService;

    @Operation(summary = "일정 생성", description = "주어진 calendarId에 새 일정을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "일정 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(
            @PathVariable Long calendarId,
            @RequestBody @Valid ScheduleRequestDto scheduleRequestDto) {
        ScheduleResponseDto responseDto = scheduleService.createSchedule(calendarId, scheduleRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "일정 수정", description = "기존 일정(scheduleId)을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "일정 수정 성공"),
            @ApiResponse(responseCode = "404", description = "일정이 존재하지 않음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PutMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(
            @PathVariable Long calendarId,
            @PathVariable Long scheduleId,
            @RequestBody @Valid ScheduleRequestDto scheduleRequestDto) {
        ScheduleResponseDto responseDto = scheduleService.updateSchedule(calendarId, scheduleId, scheduleRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "일정 삭제", description = "지정된 일정(scheduleId)을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "일정 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "일정이 존재하지 않음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(
            @PathVariable Long calendarId,
            @PathVariable Long scheduleId) {
        scheduleService.deleteSchedule(calendarId, scheduleId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "일정 목록 조회", description = "지정된 날짜 범위(startDate ~ endDate) 내의 일정을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "일정 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 날짜 형식"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> getSchedules(
            @PathVariable Long calendarId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<ScheduleResponseDto> schedules = scheduleService.getSchedules(calendarId, startDate, endDate);
        return ResponseEntity.ok(schedules);
    }

    @Operation(summary = "특정 일정 조회", description = "주어진 일정 ID(scheduleId)를 기반으로 특정 일정을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "일정 조회 성공"),
            @ApiResponse(responseCode = "404", description = "일정이 존재하지 않음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponseDto> getSchedule(
            @PathVariable Long calendarId,
            @PathVariable Long scheduleId) {
        ScheduleResponseDto responseDto = scheduleService.getScheduleById(calendarId, scheduleId);
        return ResponseEntity.ok(responseDto);
    }
}
