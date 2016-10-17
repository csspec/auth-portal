package org.csspec.auth.api;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.csspec.auth.Configuration;
import org.csspec.auth.config.JwtConfiguration;
import org.csspec.auth.db.schema.Account;
import org.csspec.auth.db.schema.ClientApplication;
import org.csspec.auth.db.schema.UserRole;
import org.csspec.auth.db.services.AuthUserService;
import org.csspec.auth.db.services.ClientApplicationDetailService;
import org.csspec.auth.exceptions.InsufficientAuthorizationException;
import org.csspec.auth.exceptions.InsufficientRoleException;
import org.csspec.auth.exceptions.InvalidAuthorizationTokenException;
import org.csspec.auth.exceptions.UnknownClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;

/**
 * request approving routines
 */
@Component
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
        if (account.getRole().getRole() > role.getRole()) {
            throw new InsufficientRoleException(account, role);
        }
        return account;
    }


    private String parseAuthorizationHeader(HttpServletRequest request) throws Exception {
        Enumeration<String> headers = request.getHeaders("Authorization");
        if (headers == null) {
            throw new InsufficientAuthorizationException();
        }
        String authToken = headers.hasMoreElements() ? headers.nextElement() : null;

        if (authToken == null)
            throw new InsufficientAuthorizationException();

        if (!authToken.trim().startsWith("Bearer "))
            return null;

        String []strings = authToken.split(" ");
        if (strings.length <= 1)
            return null;

        return strings[1];
    }

    /**
     * validate and return the authorized services
     * @param servicesAuthorized actual authorized services
     * @param referringClient client which is requesting for services
     * @return space separated list of services
     */
    public String validateServices(String servicesAuthorized, ClientApplication referringClient) {
        Collection<String> accessibleServices = referringClient.getAllowedServices();
        String []authorizedServices = servicesAuthorized.split(" ");

        // TODO: add logic for validating the service
        return servicesAuthorized;
    }

    public void approveRequestFromClient(HttpServletRequest request, ClientApplication application) throws Exception {
        String authorizationHeader = parseAuthorizationHeader(request);
        if (authorizationHeader == null || authorizationHeader.length() == 0) {
            throw new InsufficientAuthorizationException();
        }

        Map<String, Object> claims = JwtConfiguration.getClaims(authorizationHeader);
        String clientId = (String)claims.get("sub");
        if (clientId == null) {
            throw new InvalidAuthorizationTokenException();
        }

        ClientApplication clientApplication = clientDetailsService.loadClientByClientId(clientId);
        if (clientApplication == null || !application.equals(clientApplication))
            throw new UnknownClientException(clientId);

        String services = (String)claims.get("scope");
        if (services != null)
            validateServices(services, application);
    }

}
