package com.ll.TeamProject.domain.user.exceptions;

import com.ll.TeamProject.global.exceptions.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "USER_001", "아이디 또는 비밀번호가 일치하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_002", "존재하지 않는 사용자입니다."),
    INVALID_PAGE_NUMBER(HttpStatus.BAD_REQUEST, "USER_003", "페이지 번호는 1 이상이어야 합니다."),
    FORBIDDEN_NICKNAME(HttpStatus.BAD_REQUEST, "USER_004", "해당 닉네임은 사용할 수 없습니다."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "USER_005", "이미 사용중인 닉네임입니다."),
    PERMISSION_DENIED(HttpStatus.FORBIDDEN, "USER_006", "접근 권한이 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}

