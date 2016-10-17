package org.csspec.auth.exceptions;

import org.springframework.http.HttpStatus;

public class HttpForbiddenResponse extends ErrorResponse {

    public HttpForbiddenResponse(String message) {
        super(HttpStatus.FORBIDDEN.value(), message);
    }
}
