package com.ll.TeamProject.domain.user.controller;

import com.ll.TeamProject.domain.user.dto.UserDto;
import com.ll.TeamProject.domain.user.entity.SiteUser;
import com.ll.TeamProject.domain.user.service.UserService;
import com.ll.TeamProject.global.rq.Rq;
import com.ll.TeamProject.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
@Tag(name = "UserController", description = "사용자 컨트롤러")
@SecurityRequirement(name = "bearerAuth")
public class UserController {
    private final Rq rq;
    private final UserService userService;

    @GetMapping("/me")
    @Operation(summary = "내 정보")
    public UserDto me() {
        SiteUser actor = rq.findActor().get();

        return new UserDto(actor);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "회원 탈퇴 (soft)")
    public RsData<UserDto> deleteUser(@PathVariable("id") long id) {
        SiteUser userToDelete = userService.delete(id);

        return new RsData<>(
                "200-1", "회원정보가 삭제되었습니다.", new UserDto(userToDelete)
        );
    }

    record modifyUserReqBody(
            @NotBlank
            String nickname
    ) {}

    @PostMapping
    @Operation(summary = "내정보 수정")
    public RsData<Void> modifyUser(@RequestBody @Valid modifyUserReqBody reqbody) {
        userService.modify(reqbody.nickname);

        return new RsData<>(
                "200-1",
                "사용자 정보가 수정되었습니다."
        );
    }

}
