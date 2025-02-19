package com.ll.TeamProject.domain.user.controller;

import com.ll.TeamProject.domain.user.dto.LoginDto;
import com.ll.TeamProject.domain.user.service.UserService;
import com.ll.TeamProject.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "AdminAuthController", description = "관리자 로그인 컨트롤러")
@SecurityRequirement(name = "bearerAuth")
public class AdminAuthController {

    private final UserService userService;

    record UserLoginReqBody(
            @NonNull String username,
            @NonNull String password
    ) { }

    record VerificationCodeRequest(
            @NonNull String username,
            @NonNull String email
    ) { }

    record VerificationCodeVerifyRequest(
            @NonNull String username,
            @NonNull String verificationCode
    ) { }

    @PostMapping("/login")
    @Transactional
    @Operation(summary = "관리자 로그인")
    public ResponseEntity<RsData<LoginDto>> login(@RequestBody @Valid UserLoginReqBody req) {

        LoginDto loginDto = userService.login(req.username, req.password);

        return ResponseEntity.ok(
                new RsData<>(
                        "200-1",
                        "%s님 환영합니다.".formatted(loginDto.item().nickname()),
                        loginDto
                )
        );
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃")
    public ResponseEntity<RsData<Void>> logout(HttpServletRequest request) {

        userService.logout(request);

        return ResponseEntity.ok(
                new RsData<>(
                        "200-1",
                        "로그아웃 되었습니다."
                )
        );
    }

    @PostMapping("/verification-codes")
    @Operation(summary = "인증번호 발송")
    public ResponseEntity<RsData<Void>> sendVerification(@RequestBody @Valid VerificationCodeRequest req) {

        userService.processVerification(req.username, req.email);

        return ResponseEntity.ok(new RsData<>("200-1", "인증번호가 발송되었습니다.")
        );
    }

    @PostMapping("/verification-codes/verify")
    @Operation(summary = "관리자 계정 잠김 이메일 인증")
    public ResponseEntity<RsData<Void>> verificationAdminAccount(@RequestBody @Valid VerificationCodeVerifyRequest req) {
        userService.verifyAndUnlockAccount(req.username, req.verificationCode);

        return ResponseEntity.ok(new RsData<>("200-1", "인증이 완료되었습니다.")
        );
    }
}
