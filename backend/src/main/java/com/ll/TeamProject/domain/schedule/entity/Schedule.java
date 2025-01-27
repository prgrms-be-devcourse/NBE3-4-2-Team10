package com.ll.TeamProject.domain.schedule.entity;


import com.ll.TeamProject.domain.calendar.Calendar;
import com.ll.TeamProject.global.jpa.entity.BaseTime;
import com.ll.TeamProject.global.jpa.entity.Location;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Schedule extends BaseTime {
    // BaseTime : id (BaseEntity, no setter), 생성/수정일


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_id", nullable = false)
    private Calendar calendar; // 일정이 속한 캘린더

    @Column(length = 200)
    private String title; // 일정 제목

    @Column(columnDefinition = "TEXT")
    private String description; // 일정 설명 (메모)

    private LocalDateTime startTime; // 일정 시작 시간

    private LocalDateTime endTime; // 일정 종료 시간

    private Location location; // 일정 위치 정보


    public void update(String title, String description,
                       LocalDateTime startTime, LocalDateTime endTime, Location location) {
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
    }
}
