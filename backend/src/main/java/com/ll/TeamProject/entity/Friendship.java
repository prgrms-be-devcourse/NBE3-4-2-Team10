package com.ll.TeamProject.entity;

import com.ll.TeamProject.global.jpa.entity.BaseTime;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Friendship extends BaseTime {
    // BaseTime : id (BaseEntity, no setter), 생성/수정일

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 사용자 ID (FK)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id", nullable = false)
    private User friend; // 친구 사용자 ID (FK)

    private Status status; // 친구 상태 (요청, 승인, 차단)

    public enum Status {
        REQUESTED, // 요청 상태
        APPROVED,  // 승인 상태
        BLOCKED    // 차단 상태
    }
}