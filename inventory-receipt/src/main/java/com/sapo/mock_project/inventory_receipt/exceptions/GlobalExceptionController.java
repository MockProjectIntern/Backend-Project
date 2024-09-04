package com.sapo.mock_project.inventory_receipt.exceptions;

import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseUtil;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionController {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseObject<Object>> handle(IllegalArgumentException e) {
        return ResponseUtil.error400Response(e.getMessage());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ResponseObject<Object>> handle(NoResourceFoundException e) {
        return ResponseUtil.error404Response(e.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseObject<Object>> handle(ConstraintViolationException e) {
        return ResponseUtil.errorValidationResponse(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseObject<Object>> handle(Exception e) {
        log.error(e.getMessage());
        e.printStackTrace();
        return ResponseUtil.error500Response(e.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ResponseObject<Object>> handle(MissingServletRequestParameterException e) {
        return ResponseUtil.error400Response(e.getMessage());
    }
}
