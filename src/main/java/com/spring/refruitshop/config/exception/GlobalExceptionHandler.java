package com.spring.refruitshop.config.exception;

import com.spring.refruitshop.config.exception.dto.UserRegisterValidationExceptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // MethodArgumentNotValidException 을 핸들링 하는 메소드
    // 회원 가입 시 올바르지 않은 정보를 넘겨받았을 때 발생
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<UserRegisterValidationExceptionResponse> handleUserRegisterValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());      // valid 변수(userid), 에러메시지(아이디는 5글자 이상 20글자 이하입니다.)
        }// end of for() -----------------

        UserRegisterValidationExceptionResponse userRegisterValidationExceptionResponse = new UserRegisterValidationExceptionResponse(400, "Bad Request", errors);
        return ResponseEntity.badRequest().body(userRegisterValidationExceptionResponse);
    }// end of public ResponseEntity<UserRegisterValidationExceptionResponse> handleUserRegisterValidationException(MethodArgumentNotValidException ex) -------------------------------
}
