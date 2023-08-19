package org.shopping.restcontrollers;


import org.shopping.commons.CommonException;
import org.shopping.commons.rests.JSONData;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice("restcontrollers")
public class CommonRestController {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<JSONData<Object>> errorHandler(Exception e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (e instanceof CommonException) {
            CommonException commonException = (CommonException) e;
            status = commonException.getStatus();
        }

        JSONData<Object> jsonData = JSONData.builder()
                .success(false)
                .message(e.getMessage())
                .status(status)
                .build();

        return ResponseEntity.status(status).body(jsonData);
    }
}