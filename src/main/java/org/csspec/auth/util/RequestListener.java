package org.csspec.auth.util;

import org.apache.log4j.Logger;
import org.springframework.web.filter.RequestContextFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RequestListener extends RequestContextFilter {
    private static Logger logger = Logger.getLogger(RequestListener.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } finally {
            logger.info(request.getMethod() + " " + request.getRequestURI() + " " + response.getStatus());
        }
    }
}
