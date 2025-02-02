package com.ll.TeamProject.global.security;

import com.ll.TeamProject.domain.user.entity.SiteUser;
import com.ll.TeamProject.domain.user.service.UserService;
import com.ll.TeamProject.global.rq.Rq;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomOAuth2AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final UserService userService;
    private final Rq rq;

    @Value("${custom.dev.frontUrl}")
    private String devFrontUrl;

    @SneakyThrows
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        SiteUser actor = userService.findById(rq.getActor().getId()).get();

        rq.makeAuthCookies(actor);

        response.sendRedirect(devFrontUrl); // 프론트 메인 페이지로 연결
    }
}
