package com.ll.TeamProject.domain.calendar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalendarRequestDto {
    private String name;
    private String description;
}