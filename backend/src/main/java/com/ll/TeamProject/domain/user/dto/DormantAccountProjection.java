package com.ll.TeamProject.domain.user.dto;

import java.time.LocalDateTime;

public interface DormantAccountProjection {
    Long getId();
    String getNickname();
    String getEmail();
    LocalDateTime getLastLogin();
}
