package com.ll.TeamProject.domain.user.controller;

import com.ll.TeamProject.domain.user.dto.LoginDto;
import com.ll.TeamProject.domain.user.dto.UserDto;
import com.ll.TeamProject.global.mail.EmailService;
import com.ll.TeamProject.domain.user.service.UserService;
import com.ll.TeamProject.global.rsData.RsData;
import com.ll.TeamProject.standard.page.dto.PageDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
@Tag(name = "AdminController", description = "관리자 컨트롤러")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    private final UserService userService;
    private final EmailService emailService;

    record UserLoginReqBody(
            String username,
            String password
    ) {}

    @PostMapping("/login")
    @Transactional
    @Operation(summary = "관리자 로그인")
    public RsData<LoginDto> login(@RequestBody @Valid UserLoginReqBody req) {

        LoginDto loginDto = userService.login(req.username, req.password);

        return new RsData<>(
                "200-1",
                "%s님 환영합니다.".formatted(loginDto.item().nickname()),
                loginDto
        );
    }

    record VerifyCodeReqBody(
            String username,
            String email,
            String verificationCode
    ) { }

    @PostMapping("/account-verifications")
    @Operation(summary = "관리자 계정 잠김 해제")
    public RsData<Void> unlockAdminAccount(@RequestBody @Valid VerifyCodeReqBody req) {
        emailService.sendEmail(req.email(), "제목", "내용");
        return new RsData<>(
                "200-1",
                ""
        );
    }

    @GetMapping
    @Operation(summary = "회원 명단 조회 (페이징, 검색)")
    public RsData<PageDto<UserDto>> users(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "searchKeywordType", defaultValue = "username") String searchKeywordType,
            @RequestParam(name = "searchKeyword", defaultValue = "") String searchKeyword
    ) {

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
    @Operation(summary = "로그아웃")
    public RsData<Void> logout(HttpServletRequest request) {

        userService.logout(request);

        return new RsData<>(
                "200-1",
                "로그아웃 되었습니다."
        );
    }
}
