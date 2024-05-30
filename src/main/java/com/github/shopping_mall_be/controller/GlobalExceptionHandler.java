package com.github.shopping_mall_be.controller;

import com.github.shopping_mall_be.util.CartItemExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

// 오류 처리 핸들러
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
            return new ResponseEntity<>("해당 id를 가진 물건은 없습니다.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ex.getReason(), ex.getStatusCode());
    }

    @ExceptionHandler(CartItemExistsException.class)
    public ResponseEntity<Map<String, String>> handleCartItemExistsException(CartItemExistsException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT); // 409 Conflict 상태 코드 반환
    }
}
