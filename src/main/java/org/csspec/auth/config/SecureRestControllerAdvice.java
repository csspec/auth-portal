package org.csspec.auth.config;

import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SecureRestControllerAdvice {
    @ModelAttribute
    public void myMethod(RequestEntity<?> requestEntity) {
        System.out.println("Called on every request");
    }
}
