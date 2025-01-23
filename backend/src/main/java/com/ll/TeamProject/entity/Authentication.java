package com.ll.TeamProject.entity;

import com.ll.TeamProject.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Authentication extends BaseEntity {
    // BaseEntity : id (no setter)

    private Long userId; // 사용자 ID

    private AuthType authType; // 인증 유형

    private LocalDateTime lastLogin; // 마지막 로그인 시간

    private int failedAttempts; // 실패한 로그인 시도 횟수

    private boolean isLocked; // 계정 잠금 여부

    public enum AuthType {
        KAKAO, GOOGLE, SOCIAL, LOCAL
    }
}
