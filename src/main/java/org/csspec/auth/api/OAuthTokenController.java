package org.csspec.auth.api;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.csspec.auth.URIQueryParser;
import org.csspec.auth.config.JwtConfiguration;
import org.csspec.auth.db.schema.Account;
import org.csspec.auth.db.schema.ClientApplication;
import org.csspec.auth.db.schema.UserRole;
import org.csspec.auth.db.services.ClientApplicationDetailService;
import org.csspec.auth.db.services.CodeIssuer;
import org.csspec.auth.exceptions.*;
import org.csspec.auth.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * oauth related services.
 */
@RestController
@RequestMapping("/oauth")
public class OAuthTokenController {

    private ClientApplicationDetailService clientService;
    private CodeIssuer codeIssuer;
    private RequestApproval requestApproval;

    @Autowired
    public OAuthTokenController(ClientApplicationDetailService clientService, CodeIssuer codeIssuer, RequestApproval requestApproval) {
        this.clientService = clientService;
        this.codeIssuer = codeIssuer;
        this.requestApproval = requestApproval;
    }
    /**
     * returns the validated client which has the required authority as requested.
     *
     * <strong>NOTE</strong> this function looks for information about client in HTTP headers, if the required information is not
     * found then the function will throw exception.
     *
     * HttpParam client_id (required) ID of the client issued to it
     * HttpParam client_secret (optional) client_secret issued to client. Though one should never send this secret to
     *                                    client side (security issues)
     * HttpParam services (optional) services which the client wants to use. Services requested must be registered first.
     * HttpParam
     *
     * @param entity request
     * @param secretRequired whether the client secret is required to validate or not
     * @return
     */
    private ClientApplication validateAndGetClient(RequestEntity<?> entity, boolean secretRequired, boolean codeRequired) {
        HttpHeaders headers = entity.getHeaders();
        List<String> xAuthTokens = headers.get("x-auth-token");
        if (xAuthTokens == null) {
            return null;
        }
        Map<String, List<String>> credentials;
        try {
            // Do we need to parse other x-auth-token also?
            credentials = URIQueryParser.parse(xAuthTokens.get(0));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        List<String> clientIds = credentials.get("client_id");
        if (clientIds == null)
            return null;
        String clientId = clientIds.get(0);
        // we can't afford clientId to be null
        if (clientId == null)
            return null;

        List<String> clientSecrets = credentials.get("client_secret");
        String clientSecret = "";
        if (clientSecrets != null) {
            clientSecret = clientSecrets.get(0);
        }

        if (secretRequired && (clientSecret == null || clientSecret.length() == 0))
            return null;

        List<String> serviceList = credentials.get("services");
        String services = "";

        if (serviceList != null) {
            services = serviceList.get(0);
        }

        String code = null;
        List<String> codes = credentials.get("code");
        if (codes != null) {
            code = codes.get(0);
        }
        if (codeRequired && code == null)
            return null;

        ClientApplication application;
        try {
            application = clientSecret.length() == 0 ? clientService.loadClientByClientId(clientId)
                    : clientService.loadClientByIdAndSecret(clientId, clientSecret);

            if (codeRequired && !codeIssuer.isValid(code, application))
                return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        // TODO ::= check the services requested
        return application;
    }

    @RequestMapping("/issue_code")
    public ResponseEntity<?> issueCode(RequestEntity<?> entity) throws Exception {
        ClientApplication application = validateAndGetClient(entity, false, false);
        if (application == null)
            throw new InsufficientAuthorizationException();

        Map<String, Object> map = new HashMap<>();
        map.put("code", codeIssuer.issue(application, new ArrayList<>()));
        return new ResponseEntity<>(map, HttpStatus.ACCEPTED);
    }

    /**
     * returns the access token after validating the HttpHeaders i.e. whether the request comes from authorized service or not
     * @param entity should contain the authorization details in its HTTP headers under {@code x-auth-token}
     * @return a token which can be used to access the service
     */
    @RequestMapping("/access_token")
    public ResponseEntity<?> getAccessToken(RequestEntity<?> entity, HttpServletRequest request) throws Exception {
        String xAuthHeader = request.getHeader("x-auth-token");
        String []params = xAuthHeader.split("&");

        String responseType = "code";
        if (params.length >= 2) {
            for (String param : params) {
                if (param.startsWith("response_type")) {
                    String[] pair = param.split("=");
                    if (pair.length <= 1) {
                        throw new InvalidGrantTypeException();
                    }
                    responseType = pair[1];
                }
            }
        }

        if (!responseType.equals("code") && !responseType.equals("token"))
            throw new InvalidGrantTypeException();

        // user should logged in otherwise access_token will not be issued
        Account account = requestApproval.approveRequest(request, UserRole.STUDENT);
        boolean state = responseType.equals("code");
        System.out.println(state);
        ClientApplication application = validateAndGetClient(entity, state, state);
        if (application == null) {
            throw new InsufficientAuthorizationException();
        }
        final StringBuilder scope = new StringBuilder();
        application.getAllowedServices().forEach(name -> scope.append(name + " "));

        String jwt = JwtConfiguration.getJwt(application.getClientId(), scope.toString(), account.getId());
        Map<String, Object> map = new HashMap<>();
        map.put("access_token", jwt);

        // TODO::= we have to add the expiry time of the token also.
        // for implicit flow it should be small
        return new ResponseEntity<Object>(map, HttpStatus.OK);
    }

    /**
     * returns the Http status 200 plus some other information related to client if the token is valid and has not
     * expired.
     *
     * <strong>Some required parameters along with the request</strong>
     * As described in validateAndGetClient method's javadoc except that client_secret is required
     *
     * The access_token issued to the client should lie in HTTP Authorization header as
     * <code>
     *     Authorization: Bearer YOUR_ACCESS_TOKEN
     * </code>
     */
    @RequestMapping("/check_token")
    public ResponseEntity<?> checkAccessToken(HttpServletRequest request, RequestEntity<?> entity) throws Exception {
        ClientApplication application = validateAndGetClient(entity, true, false);
        if (application == null) {
            throw new InsufficientAuthorizationException();
        }

        Account account = requestApproval.approveRequestFromClient(request, application);

        return new ResponseEntity<>(account, HttpStatus.OK);
    }


    @ExceptionHandler(InsufficientAuthorizationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse insufficientAuthorizationException(InsufficientAuthorizationException exception) {
        return new ErrorResponse(403, exception.toString());
    }

    @ExceptionHandler(UnknownClientException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse unknownClientExceptionHandler(UnknownClientException exception) {
        return new ErrorResponse(403, exception.toString());
    }

    @ExceptionHandler(InvalidAuthorizationTokenException.class)
    public ErrorResponse invalidAuthorizationTokenExceptionHandler(InvalidAuthorizationTokenException exception) {
        return new ErrorResponse(403, exception.toString());
    }

    @ExceptionHandler(InvalidGrantTypeException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse invalidGrantTypeExceptionHandler(InvalidGrantTypeException exception) {
        return new ErrorResponse(403, exception.toString());
    }
}
