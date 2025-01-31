package com.ll.TeamProject.domain.calendar.entity;

import com.ll.TeamProject.domain.calendar.dto.CalendarUpdateDto;
import com.ll.TeamProject.domain.user.entity.User;
import com.ll.TeamProject.global.jpa.entity.BaseTime;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Calendar extends BaseTime {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 캘린더 소유 사용자 ID

    private String name; // 캘린더 이름

    private String description; // 캘린더 설명

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SharedCalendar> sharedUsers = new ArrayList<>(); // 공유된 사용자 정보

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messageList = new ArrayList<>();

    // 커스텀 메서드로 필드 값 설정
    public void updateName(String name) {
        this.name = name;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    protected Calendar() {
    }

    // 비즈니스 생성자
    public Calendar(User user, String name, String description) {
        this.user = user;
        this.name = name;
        this.description = description;
    }

    public void update(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
