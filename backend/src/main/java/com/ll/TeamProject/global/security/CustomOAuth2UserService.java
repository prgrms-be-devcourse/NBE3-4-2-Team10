package com.ll.TeamProject.global.security;

import com.ll.TeamProject.domain.user.entity.SiteUser;
import com.ll.TeamProject.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserService userService;

    // 소셜 로그인이 성공할 때마다 이 함수가 실행된다.
    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String oauthId = oAuth2User.getName();

        String providerTypeCode = userRequest
                .getClientRegistration()
                .getRegistrationId()
                .toUpperCase(Locale.getDefault());

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String nickname = null;
        String email = null;

        if (providerTypeCode.equals("GOOGLE")) {
            email = (String) attributes.getOrDefault("email", "");
            nickname = (String) attributes.getOrDefault("name", "");

        } else if (providerTypeCode.equals("KAKAO")) {
            Map<String, Object> properties = (Map<String, Object>) attributes.getOrDefault("properties", Map.of());
            nickname = (String) properties.getOrDefault("nickname", "");
        }

        String username = providerTypeCode + "__" + oauthId;
        SiteUser user = userService.modifyOrJoin(username, nickname, email, providerTypeCode);

        return new SecurityUser(
                user.getId(),
                user.getUsername(),
                "",
                user.getNickname(),
                user.getAuthorities()
        );
    }
}

