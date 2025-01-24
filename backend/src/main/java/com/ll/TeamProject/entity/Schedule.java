package com.ll.TeamProject.entity;


import com.ll.TeamProject.global.jpa.entity.BaseTime;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Schedule extends BaseTime {
    // BaseTime : id (BaseEntity, no setter), 생성/수정일

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_id", nullable = false)
    private Calendar calendar; // 일정이 속한 캘린더

    private String title; // 일정 제목

    private String description; // 일정 설명 (메모)

    private LocalDateTime startTime; // 일정 시작 시간

    private LocalDateTime endTime; // 일정 종료 시간

    private String location; // 일정 위치
}
