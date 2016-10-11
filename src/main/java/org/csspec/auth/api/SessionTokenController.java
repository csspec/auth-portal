package org.csspec.auth.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.csspec.auth.Configuration;
import org.csspec.auth.config.JwtConfiguration;
import org.csspec.auth.db.repositories.ClientApplicationRepository;
import org.csspec.auth.db.repositories.UserAccountRepository;
import org.csspec.auth.db.schema.Account;
import org.csspec.auth.db.schema.UserRole;
import org.csspec.auth.exceptions.ErrorResponse;
import org.csspec.auth.exceptions.InsufficientRoleException;
import org.csspec.auth.exceptions.InvalidUsernameOrPasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class SessionTokenController {
    private UserAccountRepository accountRepository;

    private ClientApplicationRepository clientRepository;

    private RequestApproval requestApproval;

    @Autowired
    public SessionTokenController(UserAccountRepository accountRepository,
                                  ClientApplicationRepository clientRepository,
                                  RequestApproval requestApproval) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
        this.requestApproval = requestApproval;
    }

    private String generateAuthToken(Account account, RequestEntity requestEntity) throws JsonProcessingException {
        return JwtConfiguration.getJwt(account.getUsername());
    }

    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public ResponseEntity<?> getAccessToken(RequestEntity<?> requestEntity,
                                            @RequestParam(value = "client_id", required = false) String client_id)
            throws Exception {

        // get the authorization header from request
        List<String> auth = requestEntity.getHeaders().get("Authorization");
        String username, password;
        String authorizationHeader;

        // parse credentials (username and password) from header
        if (auth != null && auth.get(0).startsWith("Basic")) {
            authorizationHeader = auth.get(0);
            String base64Credentials = authorizationHeader.substring("Basic".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials),
                    Charset.forName("UTF-8"));
            final String values[] = credentials.split(":", 2);
            username = values[0];
            password = values[1];
        } else {
            return new ResponseEntity<Object>(new ErrorResponse(403, "wrong credentials"), HttpStatus.FORBIDDEN);
        }

        // get account if it exists
        Account validAccount = accountRepository.findUserByName(username);
        if (validAccount == null || !validAccount.getPassword().equals(password)) {
            return new ResponseEntity<Object>(new ErrorResponse(403, "wrong credentials"), HttpStatus.FORBIDDEN);
        }

        String token = generateAuthToken(validAccount, requestEntity);
        HttpHeaders headers = new HttpHeaders();
        Date expire = new Date();
        expire.setTime(expire.getTime() + 24 * 3600 * 1000);
        System.out.println(expire.toGMTString());
        headers.add("Set-Cookie", "csspec_org=" + token + "; expires=" + expire.toGMTString());
        return new ResponseEntity<Object>(headers, HttpStatus.ACCEPTED);
    }

    @RequestMapping("/check")
    public ResponseEntity<?> checkToken(HttpServletRequest request) throws Exception {
        Account account = requestApproval.approveRequest(request, UserRole.STUDENT);
        System.out.println("Yup person was logged in: " + account.toString());;
        return new ResponseEntity<Object>(account, HttpStatus.ACCEPTED);
    }

    @RequestMapping("/logout")
    public ResponseEntity<?> logout(RequestEntity entity) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", Configuration.CSS_ORG_COOKIE_NAME + "=");
        return new ResponseEntity<Object>(headers, HttpStatus.OK);
    }

    @ExceptionHandler(InvalidUsernameOrPasswordException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse forbiddenStatusHandler(InvalidUsernameOrPasswordException exception) {
        return new ErrorResponse(403, exception.toString());
    }

    @ExceptionHandler(InvalidCookieException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse invalidCookieExceptionHandler(InvalidCookieException exception) {
        return new ErrorResponse(403, exception.toString());
    }

    @ExceptionHandler(InsufficientRoleException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse insufficientExceptionHandler(InsufficientRoleException exception) {
        return new ErrorResponse(403, exception.toString());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse internalServerError(Exception exception) {
        return new ErrorResponse(500, exception.toString());
    }
}
