package com.ll.TeamProject.global.rq;

import com.ll.TeamProject.domain.user.entity.SiteUser;
import com.ll.TeamProject.domain.user.enums.Role;
import com.ll.TeamProject.domain.user.service.UserService;
import com.ll.TeamProject.global.security.SecurityUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Arrays;
import java.util.Optional;

@RequestScope
@Component
@RequiredArgsConstructor
public class Rq {

    private final HttpServletResponse resp;
    private final HttpServletRequest req;
    private final UserService userService;

    public void setCookie(String name, String value) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .path("/")
                .domain("localhost")
                .sameSite("Strict")
                .httpOnly(true)
                .secure(true)
                .build();

        resp.addHeader("Set-Cookie", cookie.toString());
    }

    public String getHeader(String name) {
        return req.getHeader(name);
    }

    public String getCookieValue(String name) {
        return Optional
                .ofNullable(req.getCookies())
                .stream()
                .flatMap(cookies -> Arrays.stream(cookies))
                .filter(cookie -> cookie.getName().equals(name))
                .map(cookie -> cookie.getValue())
                .findFirst()
                .orElse(null);
    }

    public void setHeader(String name, String value) {
        resp.setHeader(name, value);
    }

    public void setLogin(SiteUser user) {
        // 스프링 시큐리티가 이해하는 user로 변경
        UserDetails userDetails = new SecurityUser(
                user.getId(),
                user.getUsername(),
                "",
                user.getNickname(),
                user.getAuthorities()
        );

        // 인증
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );

        // 인증 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void deleteCookie(String name) {
        ResponseCookie cookie = ResponseCookie.from(name, null)
                .path("/")
                .domain("localhost")
                .sameSite("Strict")
                .secure(true)
                .httpOnly(true)
                .maxAge(0)
                .build();

        resp.addHeader("Set-Cookie", cookie.toString());
    }

    public SiteUser getActor() {
        return Optional.ofNullable(
                        SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                )
                .map(Authentication::getPrincipal)
                .filter(principal -> principal instanceof SecurityUser)
                .map(principal -> (SecurityUser) principal)
                .map(securityUser -> new SiteUser(securityUser.getId(), securityUser.getUsername(), Role.USER))
                .orElse(null);
    }

    public String makeAuthCookies(SiteUser user) {
        String accessToken = userService.genAccessToken(user);

        setCookie("apiKey", user.getApiKey());
        setCookie("accessToken", accessToken);

        return accessToken;
    }

    public Optional<SiteUser> findByActor() {
        SiteUser actor = getActor();

        if (actor == null) {
            return Optional.empty();
        }

        return userService.findById(actor.getId());
    }
}
