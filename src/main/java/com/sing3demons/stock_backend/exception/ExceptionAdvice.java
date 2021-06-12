package com.sing3demons.stock_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String handlerproductNotFound(ProductNotFoundException ex) {
        return ex.getMessage();
    }
}
