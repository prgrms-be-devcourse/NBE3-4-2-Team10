package com.ll.TeamProject.domain.user.entity;

import com.ll.TeamProject.global.jpa.entity.BaseTime;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "\"users\"")

public class User extends BaseTime {
    // BaseTime : id (BaseEntity, no setter), 생성/수정일

    private String username; // 사용자 이름

    private String password; // 암호화된 비밀번호

    private String email; // 이메일 주소

    private Role role; // 사용자 역할 (관리자/일반 사용자)
}

