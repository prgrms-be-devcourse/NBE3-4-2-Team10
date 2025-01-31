package com.ll.TeamProject.domain.schedule.service;

import com.ll.TeamProject.domain.calendar.entity.Calendar;
import com.ll.TeamProject.domain.calendar.repository.CalendarRepository;
import com.ll.TeamProject.domain.schedule.dto.ScheduleRequestDto;
import com.ll.TeamProject.domain.schedule.dto.ScheduleResponseDto;
import com.ll.TeamProject.domain.schedule.entity.Schedule;
import com.ll.TeamProject.domain.schedule.repository.ScheduleRepository;
import com.ll.TeamProject.global.exceptions.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto scheduleRequestDto) {
        Calendar calendar = getCalendarByIdOrThrow(scheduleRequestDto.calendarId());
        Schedule schedule = getScheduleByIdOrThrow(id);

        // 기존 일정의 Calendar와 요청된 Calendar가 같은지 검증
        if (!schedule.getCalendar().getId().equals(calendar.getId())) {
            throw new ServiceException("400", "일정이 속한 캘린더가 일치하지 않습니다.");
        }

        validateScheduleForUpdate(scheduleRequestDto.calendarId(), scheduleRequestDto.startTime(), scheduleRequestDto.endTime(), id);

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
    public void deleteSchedule(Long scheduleId) {
        Schedule schedule = getScheduleByIdOrThrow(scheduleId);
        scheduleRepository.delete(schedule);
    }

    // 일정 목록 조회
    public List<ScheduleResponseDto> getSchedules(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        return scheduleRepository.findSchedulesWithinDateRange(startDateTime, endDateTime)
                .stream().map(this::mapToDto).toList();
    }

    // 특정 일정 조회
    public ScheduleResponseDto getScheduleById(Long id) {
        return mapToDto(getScheduleByIdOrThrow(id));
    }



    // Calendar 조회
    private Calendar getCalendarByIdOrThrow(Long calendarId) {
        return calendarRepository.findById(calendarId)
                .orElseThrow(() -> new ServiceException("404", "해당 캘린더를 찾을 수 없습니다."));
    }

    // Schedule 조회
    private Schedule getScheduleByIdOrThrow(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new ServiceException("404", "해당 일정을 찾을 수 없습니다."));
    }

    // 일정 생성 시 충돌 검사 (완전히 동일한 시간대 일정이 있는지 확인)
    private void validateScheduleForCreation(Long calendarId, LocalDateTime startTime, LocalDateTime endTime) {
        boolean exists = scheduleRepository.findOverlappingSchedules(calendarId, startTime, endTime)
                .stream().findAny().isPresent();

        if (exists) {
            throw new ServiceException("400", "해당 시간에 이미 일정이 존재합니다.");
        }
    }

    // 일정 수정 시 충돌 검사 (자기 자신 제외)
    private void validateScheduleForUpdate(Long calendarId, LocalDateTime startTime, LocalDateTime endTime, Long scheduleId) {
        boolean exists = scheduleRepository.findOverlappingSchedules(calendarId, startTime, endTime)
                .stream().anyMatch(schedule -> !schedule.getId().equals(scheduleId));

        if (exists) {
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
