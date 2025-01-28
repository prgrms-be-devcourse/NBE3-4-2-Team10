// CalendarCreateDto.java
package com.ll.TeamProject.domain.calendar.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CalendarCreateDto {
    private String name;
    private String description;
    private String owner;
}