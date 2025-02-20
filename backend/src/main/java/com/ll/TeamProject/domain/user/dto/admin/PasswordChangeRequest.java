package com.ll.TeamProject.domain.user.dto.admin;

import lombok.NonNull;

public record PasswordChangeRequest(
        @NonNull String password
) {}
