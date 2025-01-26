package com.ll.TeamProject.repository;

import com.ll.TeamProject.domain.calendar.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
}
