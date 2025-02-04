package com.ll.TeamProject.domain.calendar.dto;

import lombok.Data;

@Data
public abstract class CalendarDto {
    private String title; // 캘린더 제목
    private String description; // 캘린더 설명
}