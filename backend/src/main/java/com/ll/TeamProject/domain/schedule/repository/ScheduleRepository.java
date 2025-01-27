package com.ll.TeamProject.domain.schedule.repository;

import com.ll.TeamProject.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("SELECT s FROM Schedule s WHERE s.calendar.id = :calendarId AND " +
            "(s.startTime < :endTime AND s.endTime > :startTime)")
    List<Schedule> findOverlappingSchedules(@Param("calendarId") Long calendarId,
                                            @Param("startTime") LocalDateTime startTime,
                                            @Param("endTime") LocalDateTime endTime);

    // JPQL을 사용한 날짜 범위 조회
    @Query("SELECT s FROM Schedule s WHERE s.startTime >= :startDate AND s.endTime <= :endDate")
    List<Schedule> findSchedulesWithinDateRange(@Param("startDate") LocalDateTime startDate,
                                                @Param("endDate") LocalDateTime endDate);


}
