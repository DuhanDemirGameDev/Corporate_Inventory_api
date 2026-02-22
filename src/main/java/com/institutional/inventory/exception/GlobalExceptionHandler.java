package com.institutional.inventory.exception;

import com.institutional.inventory.dto.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.util.HashMap;
import java.util.Map;

import java.time.LocalDateTime;

@RestControllerAdvice // Bu anotasyon Spring'e "Tüm Controller'ları dinle, hata çıkarsa buraya yönlendir" der.
public class GlobalExceptionHandler {

    // 1. BİZİM YAZDIĞIMIZ "BULUNAMADI" HATALARINI YAKALA (404 Not Found)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                webRequest.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    // 2. İŞ KURALI (BUSINESS LOGIC) HATALARINI YAKALA (400 Bad Request)
    // Örn: Cihaz boşta değilken zimmetlemeye çalışma hatası
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(RuntimeException ex, WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                webRequest.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    // 3. VERİ DOĞRULAMA (VALIDATION) HATALARINI YAKALA
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        // Hatalı olan tüm alanları bul ve bizim yazdığımız mesajlarla eşleştir
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}