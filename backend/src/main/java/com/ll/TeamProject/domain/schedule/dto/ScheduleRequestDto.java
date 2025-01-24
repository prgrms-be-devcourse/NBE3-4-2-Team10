package com.ll.TeamProject.domain.schedule.dto;

import com.ll.TeamProject.global.jpa.entity.Location;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public record ScheduleRequestDto(
        Long calendarId,
        String title,
        String description,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Location location
){}