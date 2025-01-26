package com.ll.TeamProject.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalendarCreateDto {
    private String title;
    private String description;
    private Long[] members; // 멤버 IDs
}