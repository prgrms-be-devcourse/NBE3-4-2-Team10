package com.ll.TeamProject.global.globalExceptionHandler;

import com.ll.TeamProject.global.exceptions.CustomException;
import com.ll.TeamProject.domain.user.exceptions.UserErrorCode;
import com.ll.TeamProject.global.exceptions.ServiceException;
import com.ll.TeamProject.global.rsData.ResponseDto;
import com.ll.TeamProject.global.rsData.RsData;
import com.ll.TeamProject.standard.base.Empty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ResponseDto<Empty>> handle(NoSuchElementException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ResponseDto.failure(
                        "404-1",
                        "해당 데이터가 존재하지 않습니다."
                ));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<RsData<Empty>> handle(ServiceException ex) {
        RsData<Empty> rsData = ex.getRsData();

        return ResponseEntity
                .status(rsData.getStatusCode())
                .body(rsData);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        UserErrorCode errorCode = ex.getErrorCode();

        log.error(errorCode.getCode()+ " : " +errorCode.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                errorCode.getCode(),
                errorCode.getMessage()
        );

        return new ResponseEntity<>(errorResponse, errorCode.getStatus());
    }
}