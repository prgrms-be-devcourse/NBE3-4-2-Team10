package com.ll.TeamProject.domain.user.controller;

import com.ll.TeamProject.domain.user.dto.LoginDto;
import com.ll.TeamProject.domain.user.dto.UserDto;
import com.ll.TeamProject.domain.user.enums.Role;
import com.ll.TeamProject.domain.user.service.UserService;
import com.ll.TeamProject.global.rsData.RsData;
import com.ll.TeamProject.standard.page.dto.PageDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.NonNull;
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

    record UserLoginReqBody(
            @NonNull String username,
            @NonNull String password
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

    record VerificationCodeRequest(
            @NonNull String username,
            @NonNull String email
    ) { }

    @PostMapping("/verification-codes")
    @Operation(summary = "인증번호 발송")
    public RsData<Void> sendVerification(@RequestBody @Valid VerificationCodeRequest req) {

        userService.processVerification(req.username, req.email);

        return new RsData<>("200-1", "인증번호가 발송되었습니다.");
    }

    record VerificationCodeVerifyRequest(
            @NonNull String username,
            @NonNull String verificationCode
    ) { }

    @PostMapping("/verification-codes/verify")
    @Operation(summary = "관리자 계정 잠김 이메일 인증")
    public RsData<Void> verificationAdminAccount(@RequestBody @Valid VerificationCodeVerifyRequest req) {
        userService.verifyAndUnlockAccount(req.username, req.verificationCode);

        return new RsData<>(
                "200-1",
                "인증이 완료되었습니다."
        );
    }

    record PasswordChangeRequest(
            @NonNull String password
    ) {}

    @PatchMapping("/{username}/password")
    @Operation(summary = "관리자 비밀번호 변경")
    public RsData<Void> changePassword(@PathVariable("username") String username
            , @RequestBody PasswordChangeRequest req) {

        userService.changePassword(username, req.password);

        return new RsData<>(
                "200-1",
                    "비밀번호 변경이 완료되었습니다."
        );
    }

    @GetMapping("/users")
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
                    userService.findUsers(searchKeywordType, searchKeyword, page, pageSize, Role.USER)
                            .map(UserDto::new)
                )
        );
    }

    @GetMapping("/admins")
    @Operation(summary = "회원 명단 조회 (페이징, 검색)")
    public RsData<PageDto<UserDto>> admins(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "searchKeywordType", defaultValue = "username") String searchKeywordType,
            @RequestParam(name = "searchKeyword", defaultValue = "") String searchKeyword
    ) {

        return new RsData<>(
                "200-1",
                "",
                new PageDto<>(
                        userService.findUsers(searchKeywordType, searchKeyword, page, pageSize, Role.ADMIN)
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

    @PatchMapping("/admins/{id}")
    @Operation(summary = "관리자 잠김 풀기")
    public RsData<Void> unlockAdmins(@PathVariable("id") Long id) {
        userService.unlockAccount(id);

        return new RsData<>(
                "200-1",
                "관리자 잠금이 해제되었습니다."
        );
    }
}
