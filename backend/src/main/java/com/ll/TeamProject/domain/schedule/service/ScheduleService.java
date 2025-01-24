package com.ll.TeamProject.domain.schedule.service;

import com.ll.TeamProject.domain.calendar.Calendar;
import com.ll.TeamProject.domain.schedule.dto.ScheduleRequestDto;
import com.ll.TeamProject.domain.schedule.dto.ScheduleResponseDto;
import com.ll.TeamProject.domain.schedule.entity.Schedule;
import com.ll.TeamProject.domain.schedule.repository.ScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final CalendarRepository calendarRepository;

    //일정생성
    public ScheduleResponseDto createSchedule(Long calendarId, ScheduleRequestDto scheduleRequestDto){
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Calendar not found"));

        Schedule schedule=new Schedule(
                calendar,
                scheduleRequestDto.title(),
                scheduleRequestDto.description(),
                scheduleRequestDto.startTime(),
                scheduleRequestDto.endTime(),
                scheduleRequestDto.location()
        );

        Schedule saveSchedule=scheduleRepository.save(schedule);
        return mapToDto(saveSchedule);
    }

    //일정수정
    public ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto scheduleRequestDto) {
        // 기존 스케줄 조회
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found"));

        // 캘린더 조회
        Calendar calendar = calendarRepository.findById(scheduleRequestDto.calendarId())
                .orElseThrow(() -> new EntityNotFoundException("Calendar not found"));

        // 스케줄 정보 업데이트
        schedule.update(
                scheduleRequestDto.title(),
                scheduleRequestDto.description(),
                scheduleRequestDto.startTime(),
                scheduleRequestDto.endTime(),
                scheduleRequestDto.location()
        );

        return mapToDto(schedule);
    }

    //일정삭제
    public void deleteSchedule(Long scheduleId){
        Schedule schedule=scheduleRepository.findById(scheduleId)
                .orElseThrow(()->new EntityNotFoundException("Schedule not found"));
        scheduleRepository.delete(schedule);
    }

    //일정목록조회
    public List<ScheduleResponseDto> getSchedules(){
        return scheduleRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    //특정일정조회
    public ScheduleResponseDto getScheduleById(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found"));
        return mapToDto(schedule);
    }

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
