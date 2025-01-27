package com.ll.TeamProject.calendar.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalendarUpdateDto {
    private String title;
    private String description;
    private Long[] members; // 멤버 IDs
}