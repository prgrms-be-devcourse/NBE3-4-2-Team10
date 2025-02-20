package com.ll.TeamProject.domain.user.dto.admin;

import lombok.NonNull;

public record UserLoginReqBody(
        @NonNull String username,
        @NonNull String password
) { }
