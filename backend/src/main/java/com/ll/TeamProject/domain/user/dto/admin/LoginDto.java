package com.ll.TeamProject.domain.user.dto.admin;

import com.ll.TeamProject.domain.user.dto.UserDto;

public record LoginDto(
        UserDto item,
        String apiKey,
        String accessToken
) { }
