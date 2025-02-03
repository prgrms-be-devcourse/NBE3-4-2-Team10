package com.ll.TeamProject.domain.user.entity;

import com.ll.TeamProject.domain.user.enums.AuthType;
import com.ll.TeamProject.global.jpa.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Authentication extends BaseEntity {
    // BaseEntity : id (no setter)
    @Column(unique = true, nullable = false)
    private Long userId; // 사용자 ID

    @Enumerated(EnumType.STRING)
    private AuthType authType; // 인증 유형

    @Column
    private LocalDateTime lastLogin; // 마지막 로그인 시간

    @Column
    private int failedAttempts; // 실패한 로그인 시도 횟수

    @Column
    private boolean isLocked; // 계정 잠금 여부

    public void setLastLogin() {
        this.lastLogin = LocalDateTime.now();
    }

    public int failedLogin(int attempts) {
        this.failedAttempts = attempts;
        return failedAttempts;
    }

    public void lockAccount() {
        this.isLocked = true;
    }

    public void resetFailedAttempts() {
        this.failedAttempts = 0;
    }
}
