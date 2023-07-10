package org.example.http.handler;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(basePackages = "org.example.http.rest")
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {
}
