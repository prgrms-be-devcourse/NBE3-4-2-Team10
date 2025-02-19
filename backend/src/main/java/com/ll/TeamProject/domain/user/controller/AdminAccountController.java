package com.ll.TeamProject.domain.user.controller;

import com.ll.TeamProject.domain.user.service.UserService;
import com.ll.TeamProject.global.rsData.RsData;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
@Tag(name = "AdminAccountController", description = "관리자 계정 컨트롤러")
@SecurityRequirement(name = "bearerAuth")
public class AdminAccountController {

    private final UserService userService;

    record PasswordChangeRequest(
            @NonNull String password
    ) {}

    @PutMapping("/{username}/password")
    public ResponseEntity<RsData<Void>> changePassword(
            @PathVariable("username") String username,
            @RequestBody @Valid PasswordChangeRequest request) {
        userService.changePassword(username, request.password());
        return ResponseEntity.ok(new RsData<>("200-1", "비밀번호 변경이 완료되었습니다."));
    }

    @PatchMapping("/{id}/unlock")
    public ResponseEntity<RsData<Void>> unlockAdmin(@PathVariable("id") Long id) {
        userService.unlockAccount(id);
        return ResponseEntity.ok(new RsData<>("200-1", "관리자 잠금이 해제되었습니다."));
    }





}
