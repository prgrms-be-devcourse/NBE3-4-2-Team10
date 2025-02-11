package com.ll.TeamProject.domain.schedule.service;

import com.ll.TeamProject.domain.calendar.entity.Calendar;
import com.ll.TeamProject.domain.calendar.repository.CalendarRepository;
import com.ll.TeamProject.domain.schedule.dto.ScheduleRequestDto;
import com.ll.TeamProject.domain.schedule.dto.ScheduleResponseDto;
import com.ll.TeamProject.domain.schedule.entity.Schedule;
import com.ll.TeamProject.domain.schedule.repository.ScheduleRepository;
import com.ll.TeamProject.domain.user.entity.SiteUser;
import com.ll.TeamProject.global.exceptions.ServiceException;
import com.ll.TeamProject.global.userContext.UserContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
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
    private final UserContext userContext;

    // 일정 생성
    public ScheduleResponseDto createSchedule(Long calendarId, ScheduleRequestDto scheduleRequestDto, SiteUser user) {
        Calendar calendar = validateCalendarOwner(calendarId, user);
//        validateScheduleForCreation(calendarId, scheduleRequestDto.startTime(), scheduleRequestDto.endTime());

        Schedule schedule = new Schedule(
                calendar,
                scheduleRequestDto.title(),
                scheduleRequestDto.description(),
                user,
                scheduleRequestDto.startTime(),
                scheduleRequestDto.endTime(),
                scheduleRequestDto.location()
        );

        return mapToDto(scheduleRepository.save(schedule));
    }

    // 일정 수정
    public ScheduleResponseDto updateSchedule(Long calendarId, Long scheduleId, ScheduleRequestDto scheduleRequestDto, SiteUser user) {
        Calendar calendar = validateCalendarOwner(calendarId, user);
        Schedule schedule = getScheduleByIdOrThrow(scheduleId);

        if (!schedule.getCalendar().getId().equals(calendarId)) {
            throw new ServiceException("400", "해당 일정은 요청한 캘린더에 속하지 않습니다.");
        }

        if (!schedule.getUser().getId().equals(user.getId())) {
            throw new ServiceException("403", "일정을 수정할 권한이 없습니다.");
        }

//        validateScheduleForUpdate(calendarId, scheduleRequestDto.startTime(), scheduleRequestDto.endTime(), scheduleId);
        schedule.update(scheduleRequestDto.title(), scheduleRequestDto.description(), scheduleRequestDto.startTime(), scheduleRequestDto.endTime(), scheduleRequestDto.location());
        return mapToDto(schedule);
    }

    // 일정 삭제
    public void deleteSchedule(Long calendarId, Long scheduleId, SiteUser user) {
        validateCalendarOwner(calendarId, user);
        Schedule schedule = getScheduleByIdOrThrow(scheduleId);

        if (!schedule.getCalendar().getId().equals(calendarId)) {
            throw new ServiceException("400", "해당 일정은 요청한 캘린더에 속하지 않습니다.");
        }

        if (!schedule.getUser().getId().equals(user.getId())) {
            throw new ServiceException("403", "일정을 삭제할 권한이 없습니다.");
        }

        scheduleRepository.delete(schedule);
    }

    // 특정 캘린더의 일정 목록 조회
    public List<ScheduleResponseDto> getSchedules(Long calendarId, LocalDate startDate, LocalDate endDate, SiteUser user) {
        validateCalendarOwner(calendarId, user);

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        return scheduleRepository.findSchedulesByCalendarAndDateRange(calendarId, startDateTime, endDateTime)
                .stream().map(this::mapToDto).toList();
    }

    // 하루 일정 조회
    public List<ScheduleResponseDto> getDailySchedules(Long calendarId, LocalDate date, SiteUser user) {
        validateCalendarOwner(calendarId, user);

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        return scheduleRepository.findSchedulesByCalendarAndDateRange(calendarId, startOfDay, endOfDay)
                .stream().map(this::mapToDto).toList();
    }

    //일주일 일정 조회 (일요일 기준)
    public List<ScheduleResponseDto> getWeeklySchedules(Long calendarId, LocalDate date, SiteUser user){
        validateCalendarOwner(calendarId, user);

        // 기준 날짜가 포함된 주의 일요일 및 토요일 계산
        LocalDate startOfWeek = date.with(DayOfWeek.SUNDAY);
        LocalDate endOfWeek = date.with(DayOfWeek.SATURDAY);

        LocalDateTime startDateTime = startOfWeek.atStartOfDay();
        LocalDateTime endDateTime = endOfWeek.atTime(LocalTime.MAX);

        return scheduleRepository.findSchedulesByCalendarAndDateRange(calendarId, startDateTime, endDateTime)
                .stream().map(this::mapToDto).toList();
    }

    // 한 달 일정 조회 (1일부터 말일까지)
    public List<ScheduleResponseDto> getMonthlySchedules(Long calendarId, LocalDate date, SiteUser user) {
        validateCalendarOwner(calendarId, user);

        // 기준 날짜의 월의 첫째 날과 마지막 날 계산
        LocalDate firstDayOfMonth = date.withDayOfMonth(1);
        LocalDate lastDayOfMonth = date.withDayOfMonth(date.lengthOfMonth());

        LocalDateTime startDateTime = firstDayOfMonth.atStartOfDay();
        LocalDateTime endDateTime = lastDayOfMonth.atTime(LocalTime.MAX);

        return scheduleRepository.findSchedulesByCalendarAndDateRange(calendarId, startDateTime, endDateTime)
                .stream().map(this::mapToDto).toList();
    }

    // 특정 일정 조회
    public ScheduleResponseDto getScheduleById(Long calendarId, Long scheduleId, SiteUser user) {
        validateCalendarOwner(calendarId, user);
        Schedule schedule = getScheduleByIdOrThrow(scheduleId);

        if (!schedule.getCalendar().getId().equals(calendarId)) {
            throw new ServiceException("400", "해당 일정은 요청한 캘린더에 속하지 않습니다.");
        }

        return mapToDto(schedule);
    }

    // 캘린더 존재 확인
    private Calendar getCalendarByIdOrThrow(Long calendarId) {
        return calendarRepository.findById(calendarId)
                .orElseThrow(() -> new ServiceException("404", "해당 캘린더를 찾을 수 없습니다."));
    }

    // 일정 존재 확인
    private Schedule getScheduleByIdOrThrow(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ServiceException("404", "해당 일정을 찾을 수 없습니다."));
    }

//    // 일정 생성 시 충돌 검사
//    private void validateScheduleForCreation(Long calendarId, LocalDateTime startTime, LocalDateTime endTime) {
//        List<Schedule> overlappingSchedules = scheduleRepository.findOverlappingSchedules(calendarId, startTime, endTime);
//        if (!overlappingSchedules.isEmpty()) {
//            throw new ServiceException("400", "해당 시간에 이미 일정이 존재합니다.");
//        }
//    }
//
//    // 일정 수정 시 충돌 검사
//    private void validateScheduleForUpdate(Long calendarId, LocalDateTime startTime, LocalDateTime endTime, Long scheduleId) {
//        List<Schedule> overlappingSchedules = scheduleRepository.findOverlappingSchedules(calendarId, startTime, endTime);
//        boolean hasConflict = overlappingSchedules.stream()
//                .anyMatch(schedule -> !schedule.getId().equals(scheduleId));
//
//        if (hasConflict) {
//            throw new ServiceException("400", "해당 시간에 이미 일정이 존재합니다.");
//        }
//    }

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

    // 현재 인증된 사용자 가져오기
    public SiteUser getAuthenticatedUser() {
        return userContext.findActor()
                .orElseThrow(() -> new ServiceException("401", "로그인을 먼저 해주세요!"));
    }

    // 캘린더 소유자 검증
    private Calendar validateCalendarOwner(Long calendarId, SiteUser user) {
        Calendar calendar = getCalendarByIdOrThrow(calendarId);
        if (!calendar.getUser().getId().equals(user.getId())) {
            throw new ServiceException("403", "캘린더 소유자만 접근할 수 있습니다.");
        }
        return calendar;
    }
}
