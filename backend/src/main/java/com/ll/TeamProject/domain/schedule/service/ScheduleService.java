package com.ll.TeamProject.domain.schedule.service;

import com.ll.TeamProject.domain.calendar.entity.Calendar;
import com.ll.TeamProject.domain.calendar.repository.CalendarRepository;
import com.ll.TeamProject.domain.schedule.dto.ScheduleRequestDto;
import com.ll.TeamProject.domain.schedule.dto.ScheduleResponseDto;
import com.ll.TeamProject.domain.schedule.entity.Schedule;
import com.ll.TeamProject.domain.schedule.repository.ScheduleRepository;
import com.ll.TeamProject.global.exceptions.ServiceException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final CalendarRepository calendarRepository;


    // 일정 생성
    public ScheduleResponseDto createSchedule(Long calendarId, ScheduleRequestDto scheduleRequestDto) {
        Calendar calendar = getCalendarByIdOrThrow(calendarId);

        validateScheduleForCreation(calendarId, scheduleRequestDto.startTime(), scheduleRequestDto.endTime());

        Schedule schedule = new Schedule(
                calendar,
                scheduleRequestDto.title(),
                scheduleRequestDto.description(),
                scheduleRequestDto.startTime(),
                scheduleRequestDto.endTime(),
                scheduleRequestDto.location()
        );

        return mapToDto(scheduleRepository.save(schedule));
    }

    // 일정 수정
    public ScheduleResponseDto updateSchedule(Long calendarId, Long scheduleId, ScheduleRequestDto scheduleRequestDto) {
        // 1. 해당 캘린더 존재 여부 확인
        Calendar calendar = getCalendarByIdOrThrow(calendarId);

        // 2. 일정 존재 여부 및 calendarId 일치 여부 확인
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ServiceException("404", "해당 일정을 찾을 수 없습니다."));

        if (!schedule.getCalendar().getId().equals(calendarId)) {
            throw new ServiceException("400", "해당 일정은 요청한 캘린더에 속하지 않습니다.");
        }

        // 3. 일정 시간 중복 검증 (중복이 있으면 예외 발생)
        validateScheduleForUpdate(calendarId, scheduleRequestDto.startTime(), scheduleRequestDto.endTime(), scheduleId);

        // 4. 일정 업데이트
        schedule.update(
                scheduleRequestDto.title(),
                scheduleRequestDto.description(),
                scheduleRequestDto.startTime(),
                scheduleRequestDto.endTime(),
                scheduleRequestDto.location()
        );

        return mapToDto(schedule);
    }


    // 일정 삭제
    public void deleteSchedule(Long calendarId, Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ServiceException("404", "해당 일정을 찾을 수 없습니다."));

        if (!schedule.getCalendar().getId().equals(calendarId)) {
            throw new ServiceException("400", "해당 일정은 요청한 캘린더에 속하지 않습니다.");
        }
        scheduleRepository.delete(schedule);
    }

    // 특정 캘린더의 일정 목록 조회
    public List<ScheduleResponseDto> getSchedules(Long calendarId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        return scheduleRepository.findSchedulesByCalendarAndDateRange(calendarId, startDateTime, endDateTime)
                .stream().map(this::mapToDto).toList();
    }

    // 하루 일정 조회 (00:00 ~ 23:59:59)
    public List<ScheduleResponseDto> getDailySchedules(Long calendarId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay(); // 00:00:00
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX); // 23:59:59

        return scheduleRepository.findSchedulesByCalendarAndDateRange(calendarId, startOfDay, endOfDay)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    // 특정 일정 조회
    public ScheduleResponseDto getScheduleById(Long calendarId, Long scheduleId) {
        // 1. 일정이 존재하는지 먼저 확인
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ServiceException("404", "해당 일정을 찾을 수 없습니다."));

        // 2. 일정이 해당 calendarId에 속하는지 확인
        if (!schedule.getCalendar().getId().equals(calendarId)) {
            throw new ServiceException("400", "해당 일정은 요청한 캘린더에 속하지 않습니다.");
        }

        // 3. DTO 변환 후 반환
        return mapToDto(schedule);
    }



    // Calendar 존재 확인
    private Calendar getCalendarByIdOrThrow(Long calendarId) {

        return calendarRepository.findById(calendarId)
                .orElseThrow(() -> new ServiceException("404", "해당 캘린더를 찾을 수 없습니다."));
    }


    // 일정 생성 시 충돌 검사
    private void validateScheduleForCreation(Long calendarId, LocalDateTime startTime, LocalDateTime endTime) {
        List<Schedule> overlappingSchedules = scheduleRepository.findOverlappingSchedules(calendarId, startTime, endTime);

        if (!overlappingSchedules.isEmpty()) { // 리스트가 비어 있지 않다면 중복됨
            throw new ServiceException("400", "해당 시간에 이미 일정이 존재합니다.");
        }
    }


    // 일정 수정 시 충돌 검사
    private void validateScheduleForUpdate(Long calendarId, LocalDateTime startTime, LocalDateTime endTime, Long scheduleId) {
        List<Schedule> overlappingSchedules = scheduleRepository.findOverlappingSchedules(calendarId, startTime, endTime);

        boolean hasConflict = overlappingSchedules.stream()
                .anyMatch(schedule -> !schedule.getId().equals(scheduleId)); //  본인의 일정 제외하고 중복 검사

        if (hasConflict) {
            throw new ServiceException("400", "해당 시간에 이미 일정이 존재합니다.");
        }
    }


    // DTO 변환
    private ScheduleResponseDto mapToDto(Schedule schedule) {
        return new ScheduleResponseDto(
                schedule.getId(),
                schedule.getCalendar().getId(),
                schedule.getTitle(),
                schedule.getDescription(),
                schedule.getStartTime(),
                schedule.getEndTime(),
                schedule.getLocation(),
                schedule.getCreateDate(),
                schedule.getModifyDate()
        );
    }
}
