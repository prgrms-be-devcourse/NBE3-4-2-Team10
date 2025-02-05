package com.ll.TeamProject.domain.schedule.repository;

import com.ll.TeamProject.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    // 특정 캘린더에서 겹치는 일정 조회
    @Query("""
    SELECT s FROM Schedule s WHERE s.calendar.id = :calendarId AND\s
    (s.startTime < :endTime AND s.endTime > :startTime)
   \s""")
    List<Schedule> findOverlappingSchedules(@Param("calendarId") Long calendarId,
                                            @Param("startTime") LocalDateTime startTime,
                                            @Param("endTime") LocalDateTime endTime);


    // 특정 캘린더 내 일정 조회 (기존 쿼리에서 calendarId 추가)
    @Query("SELECT s FROM Schedule s WHERE s.calendar.id = :calendarId AND " +
            "s.startTime >= :startDate AND s.endTime <= :endDate")
    List<Schedule> findSchedulesByCalendarAndDateRange(@Param("calendarId") Long calendarId,
                                                       @Param("startDate") LocalDateTime startDate,
                                                       @Param("endDate") LocalDateTime endDate);


    Optional<Schedule> findTopByOrderByIdDesc();
}
