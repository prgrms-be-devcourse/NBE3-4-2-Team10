package com.ll.TeamProject.domain.user.dto;

import com.ll.TeamProject.domain.user.entity.SiteUser;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserDto {
    private long id;
    private String username;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    public UserDto(SiteUser siteUser) {
        this.id = siteUser.getId();
        this.username = siteUser.getUsername();
        this.createDate = siteUser.getCreateDate();
        this.modifyDate = siteUser.getModifyDate();
    }
}
