package com.ll.TeamProject.domain.user.entity;

import com.ll.TeamProject.domain.user.enums.AuthType;
import com.ll.TeamProject.global.jpa.entity.BaseEntity;
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
    private Long userId; // 사용자 ID

    @Enumerated(EnumType.STRING)
    private AuthType authType; // 인증 유형

    private LocalDateTime lastLogin; // 마지막 로그인 시간

    private int failedAttempts; // 실패한 로그인 시도 횟수

    private boolean isLocked; // 계정 잠금 여부

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public int failedLogin(int attempts) {
        this.failedAttempts = attempts;
        return failedAttempts;
    }

    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public enum AuthType {
        KAKAO, GOOGLE, SOCIAL, LOCAL
    }
}
