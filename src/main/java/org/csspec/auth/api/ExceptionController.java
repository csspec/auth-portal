package org.csspec.auth.api;

import org.csspec.auth.exceptions.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * TODO: We need an intuitive way to handle all the exceptions
 */
@Controller
public class ExceptionController {
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorResponse methodNotAllowedHandler(HttpRequestMethodNotSupportedException exception) {
        return new ErrorResponse(HttpStatus.METHOD_NOT_ALLOWED.value(), exception.toString());
    }
}
