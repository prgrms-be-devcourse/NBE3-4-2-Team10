package com.ll.TeamProject.entity;

import com.ll.TeamProject.global.jpa.entity.BaseTime;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Calendar extends BaseTime {
    // BaseTime : id (BaseEntity, no setter), 생성/수정일

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 캘린더 소유 사용자 ID

    private String name; // 캘린더 이름

    private String description; // 캘린더 설명

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SharedCalendar> sharedUsers = new ArrayList<>(); // 공유된 사용자 정보

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messageList = new ArrayList<>();
}