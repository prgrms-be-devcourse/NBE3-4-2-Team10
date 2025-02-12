// CalendarUpdateDto.java
package com.ll.TeamProject.domain.calendar.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CalendarUpdateDto {
    private String name;
    private String description;
}