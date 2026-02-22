package com.institutional.inventory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Bu hata fırlatıldığında otomatik olarak 404 (Not Found) kodu dönsün diyoruz.
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message); // Mesajı üst sınıfa (RuntimeException) iletiyoruz
    }
}