package com.ll.TeamProject.domain.user.dto;

import com.ll.TeamProject.domain.user.entity.SiteUser;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserDto {
    private long id;
    private String username;
    private String nickname;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private String email;

    public UserDto(SiteUser siteUser) {
        this.id = siteUser.getId();
        this.username = siteUser.getUsername();
        this.nickname = siteUser.getNickname();
        this.createDate = siteUser.getCreateDate();
        this.modifyDate = siteUser.getModifyDate();
        this.email = siteUser.getEmail();
    }
}
