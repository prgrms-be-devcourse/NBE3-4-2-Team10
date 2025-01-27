package com.ll.TeamProject.calendar.repository;

import com.ll.TeamProject.calendar.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
}
