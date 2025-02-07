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
    @Column
    private Long userId;

    @Enumerated(EnumType.STRING)
    private AuthType authType;

    @Column
    private LocalDateTime lastLogin;

    @Column
    private int failedAttempts;

    public void setLastLogin() {
        this.lastLogin = LocalDateTime.now();
    }

    public int failedLogin() {
        this.failedAttempts = this.failedAttempts + 1;
        return failedAttempts;
    }

    public void resetFailedAttempts() {
        this.failedAttempts = 0;
    }
}
