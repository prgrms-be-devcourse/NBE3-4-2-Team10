package com.ll.TeamProject.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoUserInfo {
    private String id;           // 카카오에서 제공하는 유저 고유 ID
    private String nickname;   // 사용자 닉네임
    private String email;

    // 생성자
    public KakaoUserInfo(Long id, String nickname) {
        this.id = String.valueOf(id);
        this.email = id + "@temp.com";
        this.nickname = nickname;
    }

    public KakaoUserInfo(String email) {
        this.email = email;
        this.id = email.substring(0, email.indexOf("@"));
    }

    @Override
    public String toString() {
        return "KakaoUserInfo{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}