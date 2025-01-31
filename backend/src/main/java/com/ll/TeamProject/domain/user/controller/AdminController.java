package com.ll.TeamProject.domain.user.controller;

import com.ll.TeamProject.domain.user.dto.UserDto;
import com.ll.TeamProject.domain.user.entity.SiteUser;
import com.ll.TeamProject.domain.user.service.UserService;
import com.ll.TeamProject.global.exceptions.ServiceException;
import com.ll.TeamProject.global.rq.Rq;
import com.ll.TeamProject.global.rsData.RsData;
import com.ll.TeamProject.standard.page.dto.PageDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final Rq rq;
    private final PasswordEncoder passwordEncoder;

    record UserLoginReqBody(
            String username,
            String password
    ) {}

    record UserLoginResBody(
            UserDto item,
            String apiKey,
            String accessToken
    ) {}

    @PostMapping("/login")
    @Transactional(readOnly = true)
    public RsData<UserLoginResBody> login(@RequestBody @Valid UserLoginReqBody req) {
        SiteUser user = userService
                .findByUsername(req.username)
                .orElseThrow(() -> new ServiceException("401-1", "존재하지 않는 사용자입니다."));

        if (!passwordEncoder.matches(req.password, user.getPassword()))
            throw new ServiceException("401-2", "비밀번호가 일치하지 않습니다.");

        String accessToken =  userService.genAccessToken(user);

        rq.setCookie("accessToken", accessToken);
        rq.setCookie("apiKey", user.getApiKey());

        return new RsData<>(
                "200-1",
                "%s님 환영합니다.".formatted(user.getUsername()),
                new UserLoginResBody(
                        new UserDto(user),
                        user.getApiKey(),
                        accessToken
                )
        );
    }

    @GetMapping
    public RsData<PageDto<UserDto>> users(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "username") String searchKeywordType,
            @RequestParam(defaultValue = "") String searchKeyword
    ) {

        if (page < 1)
            throw new ServiceException("400-1", "페이지 번호는 1 이상이어야 합니다.");

        return new RsData<>(
                "200-1",
                "",
                new PageDto<>(
                    userService.findUsers(searchKeywordType, searchKeyword, page, pageSize)
                            .map(UserDto::new)
                )
        );
    }

    @DeleteMapping("/logout")
    public RsData<Void> logout() {

        rq.deleteCookie("accessToken");
        rq.deleteCookie("apiKey");

        return new RsData<>(
                "200-1",
                "로그아웃 되었습니다."
        );
    }
}
