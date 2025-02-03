package com.ll.TeamProject.domain.user.controller;

import com.ll.TeamProject.domain.user.dto.UserDto;
import com.ll.TeamProject.domain.user.entity.SiteUser;
import com.ll.TeamProject.global.rq.Rq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
@Tag(name = "UserController", description = "사용자 컨트롤러")
@SecurityRequirement(name = "bearerAuth")
public class UserController {
    private final Rq rq;

    @GetMapping("/me")
    @Operation(summary = "내 정보")
    public UserDto me() {
        SiteUser actor = rq.findByActor().get();

        return new UserDto(actor);
    }

}
