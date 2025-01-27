package com.ll.TeamProject.domain.user.entity;

import com.ll.TeamProject.global.jpa.entity.BaseTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiteUser extends BaseTime {
    // BaseTime : id (BaseEntity, no setter), 생성/수정일

    @Column(unique = true)
    private String username; // 사용자 이름

    @Column
    private String password; // 암호화된 비밀번호

    @Column(unique = true)
    private String email; // 이메일 주소

    @Column
    @Enumerated(EnumType.STRING)
    private Role role; // 사용자 역할 (관리자/일반 사용자)

    @Column
    private String apiKey;

    public SiteUser(long id, String username, Role role) {
        super();
        this.setId(id);
        this.username = username;
        this.role = role;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getAuthoritiesAsString()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    private List<String> getAuthoritiesAsString() {
        List<String> authorities = new ArrayList<>();

        if (isAdmin()) authorities.add("ROLE_ADMIN");

        return authorities;
    }

    private boolean isAdmin() {
        return this.role == Role.ADMIN;
    }
}