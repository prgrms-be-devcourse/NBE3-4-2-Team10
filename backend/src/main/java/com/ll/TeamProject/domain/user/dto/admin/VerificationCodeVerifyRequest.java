package com.ll.TeamProject.domain.user.dto.admin;

import lombok.NonNull;

public record VerificationCodeVerifyRequest(
        @NonNull String username,
        @NonNull String verificationCode
) { }
