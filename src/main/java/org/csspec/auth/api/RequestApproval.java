package org.csspec.auth.api;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.csspec.auth.Configuration;
import org.csspec.auth.config.JwtConfiguration;
import org.csspec.auth.db.schema.Account;
import org.csspec.auth.db.schema.UserRole;
import org.csspec.auth.db.services.AuthUserService;
import org.csspec.auth.db.services.ClientApplicationDetailService;
import org.csspec.auth.exceptions.InsufficientRoleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;

import javax.servlet.http.Cookie;

/**
 * request approving routines
 */
public class RequestApproval {
    private AuthUserService service;

    private ClientApplicationDetailService clientDetailsService;

    @Autowired
    RequestApproval(AuthUserService service, ClientApplicationDetailService clientDetailsService) {
        this.service = service;
        this.clientDetailsService = clientDetailsService;
    }

    public ClientApplicationDetailService getClientDetailsService() {
        return clientDetailsService;
    }

    public AuthUserService userService() {
        return service;
    }

    public Account approveRequest(HttpServletRequest request, UserRole role) throws Exception {
        Cookie[] cookies = request.getCookies();
        String cookiePayload = "";
        for (Cookie cookie :
                cookies) {
            if (cookie.getName().equals(Configuration.CSS_ORG_COOKIE_NAME)) {
                cookiePayload = cookie.getValue();
                break;
            }
        }
        if (cookiePayload.equals("")) {
            throw new InvalidCookieException("Invalid cookie");
        }

        Account account;
        String username = JwtConfiguration.verifyJwt(cookiePayload);
        account = service.loadUserByUsername(username);
        if (account == null) {
            throw new InvalidCookieException("Invalid cookie");
        }
        if (account.getRole().getRole() < role.getRole()) {
            throw new InsufficientRoleException(account, role);
        }
        return account;
    }
}
