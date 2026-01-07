package com.ticktickdoc.handler;

import com.ticktickdoc.dto.ErrorResponseDto;
import com.ticktickdoc.exception.AuthenticationException;
import com.ticktickdoc.exception.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@RestControllerAdvice
public class Handler {

    /**
     * Bad Request (400)
     *
     * @param ex исключение
     * @return кастомный объект исключения
     */
    @ExceptionHandler({
            AuthenticationException.InvalidJwtTokenException.class
    })
    public ResponseEntity<ErrorResponseDto> handleBadRequest(Exception ex) {
        ErrorResponseDto error = new ErrorResponseDto().toBuilder()
                .message(ex.getMessage())
                .timestamp(OffsetDateTime.now(ZoneOffset.UTC))
                .code(HttpStatus.BAD_REQUEST.value())
                .build();
        return ResponseEntity.status(error.getCode()).body(error);
    }

    /**
     * Not Found (404)
     *
     * @param ex исключение
     * @return кастомный объект исключения
     */
    @ExceptionHandler({
            UserException.NullUserException.class
    })
    public ResponseEntity<ErrorResponseDto> handleNotFound(Exception ex) {
        ErrorResponseDto error = new ErrorResponseDto().toBuilder()
                .message(ex.getMessage())
                .timestamp(OffsetDateTime.now(ZoneOffset.UTC))
                .code(HttpStatus.NOT_FOUND.value())
                .build();
        return ResponseEntity.status(error.getCode()).body(error);
    }

    /**
     * Not Found (409)
     *
     * @param ex исключение
     * @return кастомный объект исключения
     */
    @ExceptionHandler({
            UserException.ConflictRegistrationUserException.class,
            AuthenticationException.ConflictAuthException.class,
            UserException.ConflictAddChildUserException.class,
            UserException.ConflictAddChildCurrentUserException.class,
            UserException.ConflictAddChildDuplicateUserException.class
    })
    public ResponseEntity<ErrorResponseDto> handleConflict(Exception ex) {
        ErrorResponseDto error = new ErrorResponseDto().toBuilder()
                .message(ex.getMessage())
                .timestamp(OffsetDateTime.now(ZoneOffset.UTC))
                .code(HttpStatus.CONFLICT.value())
                .build();
        return ResponseEntity.status(error.getCode()).body(error);
    }
}
