package com.ll.TeamProject.domain.calendar.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalendarCreateDto {
    private Long userId;  // 사용자의 ID 추가
    private String name;
    private String description;

    //생성자 추가
    public CalendarCreateDto(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public CalendarCreateDto() {

    }
}