package com.ll.TeamProject.domain.schedule.dto;

import com.ll.TeamProject.global.jpa.entity.Location;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public record ScheduleResponseDto(
        Long id,
        Long calendarId,
        String title,
        String description,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Location location,
        LocalDateTime createDate, // BaseTime의 createDate
        LocalDateTime modifyDate  // BaseTime의 modifyDate
) {}
