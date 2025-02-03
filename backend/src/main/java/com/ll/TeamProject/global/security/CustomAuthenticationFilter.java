package com.ll.TeamProject.global.security;

import com.ll.TeamProject.domain.user.entity.SiteUser;
import com.ll.TeamProject.domain.user.service.AuthenticationService;
import com.ll.TeamProject.domain.user.service.UserService;
import com.ll.TeamProject.global.rq.Rq;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends OncePerRequestFilter {

    private final Rq rq;
    private final UserService userService;
    private final AuthenticationService authenticationService;

    record AuthTokens(
            String apiKey,
            String accessToken
    ) { }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // /admin 으로 시작 하지 않으면 건너뜀
        if (!request.getRequestURI().startsWith("/admin") && !request.getRequestURI().startsWith("/user")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 로그인, 로그아웃에선 건너뜀
        if (List.of("/admin/login", "/admin/logout").contains(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        // 요청에서 토큰 얻기
        AuthTokens authTokens = getAuthTokensFromRequest();

        // 토큰이 없으면 건너뜀
        if (authTokens == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String apiKey = authTokens.apiKey;
        String accessToken = authTokens.accessToken;

        // 토큰으로 user 얻기 -> JWT로 얻은 가짜 user 객체(DB에서 조회한 user 아님) id, username, role 있는 user
        SiteUser user = userService.getUserFromAccessToken(accessToken);

        // 토큰이 만료된거면 새로 refresh
        if (user == null)
            user = refreshAccessTokenByApiKey(apiKey);

        // user 있으면 로그인 처리
        if (user != null)
            rq.setLogin(user);

        filterChain.doFilter(request, response);
    }

    // apiKey로 JWT 토큰 재생성
    private SiteUser refreshAccessTokenByApiKey(String apiKey) {
        // apiKey로 user 찾음
        Optional<SiteUser> opUser = userService.findByApiKey(apiKey);

        if (opUser.isEmpty()) return null;

        SiteUser user = opUser.get();

        // 토큰 재발급 -> 헤더와 쿠키에 토큰 설정 완료
        refreshAccessToken(user);

        return user;
    }

    private void refreshAccessToken(SiteUser user) {
        String newAccessToken = userService.genAccessToken(user);

        // 토큰 재발급 후 헤더와 쿠키에 재설정
        rq.setHeader("Authorization", "Bearer " + user.getApiKey() + " " + newAccessToken);
        rq.setCookie("accessToken", newAccessToken);
    }

    // 요청애서 토큰 얻기
    private AuthTokens getAuthTokensFromRequest() {
        // 요청 헤더에서 Authorization 얻기
        String authorization = rq.getHeader("Authorization");

        // Authorization null 아니고 Bearer 시작하면 토큰값 얻기
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring("Bearer ".length());
            String[] tokenBits = token.split(" ", 2);

            if (tokenBits.length == 2)
                return new AuthTokens(tokenBits[0], tokenBits[1]);
        }

        // 헤더에 토큰이 없다면 쿠키에서 토큰값 얻기
        String apikey = rq.getCookieValue("apiKey");
        String accessToken = rq.getCookieValue("accessToken");

        if (apikey != null && accessToken != null)
            return new AuthTokens(apikey, accessToken);

        return null;
    }
}
