package com.ll.TeamProject.domain.calendar.repository;

import com.ll.TeamProject.domain.calendar.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    List<Calendar> findByUserId(Long userId);
}