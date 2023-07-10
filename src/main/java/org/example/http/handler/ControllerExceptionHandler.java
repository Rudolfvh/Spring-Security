package org.example.http.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice(basePackages = "org.example.http.controller")
public class ControllerExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handlerException(MethodArgumentNotValidException exception) {
        log.error("Failed to return response", exception);
        if (exception.getMessage().contains("Age would be more than 18")) {
            return "error/age-error";
        }
        return "error/error500";
    }


}
