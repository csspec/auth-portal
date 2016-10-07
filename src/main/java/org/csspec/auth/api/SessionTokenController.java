package org.csspec.auth.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.csspec.auth.config.JwtConfiguration;
import org.csspec.auth.db.repositories.ClientApplicationRepository;
import org.csspec.auth.db.repositories.UserAccountRepository;
import org.csspec.auth.db.schema.Account;
import org.csspec.auth.exceptions.ErrorResponse;
import org.csspec.auth.exceptions.HttpForbiddenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.HttpCookie;
import java.nio.charset.Charset;
import java.util.*;

@RestController
@RequestMapping("/auth")
public class SessionTokenController {
    private UserAccountRepository accountRepository;

    private ClientApplicationRepository clientRepository;

    Map<String, String> tokens = new HashMap<>();

    @Autowired
    public SessionTokenController(UserAccountRepository accountRepository, ClientApplicationRepository clientRepository) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
    }
    /**
     * validates the account by extracting the {@code Authorization} header.
     * TODO: we need a better way to handle the authentication
     * @param requestEntity
     * @return true if valid else false
     */
    private boolean validateAccount(RequestEntity<?> requestEntity) {
        return false;
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
    public ResponseEntity<?> checkToken(RequestEntity entity) {
        List<String> cookies = entity.getHeaders().get("Cookie");
        if (cookies == null)
            return new ResponseEntity<Object>(HttpStatus.FORBIDDEN);
        String cookiePayload = "";
        for (String cookie :
                cookies) {
            System.out.println(cookie);
            List<HttpCookie> httpCookies = HttpCookie.parse(cookie);
            for (HttpCookie httpCookie : httpCookies) {
                if (httpCookie.getName().equals("csspec_org")) {
                    cookiePayload = httpCookie.getValue();
                }
            }
        }
        if (cookiePayload.equals(""))
            return new ResponseEntity<Object>(HttpStatus.FORBIDDEN);

        System.out.println("reached here");
        Account account;
        try {
            String username = JwtConfiguration.verifyJwt(cookiePayload);
            account = accountRepository.findUserByName(username);
            if (account == null) {
                return new ResponseEntity<Object>(new HttpForbiddenResponse("Request forbidden"), HttpStatus.FORBIDDEN);
            }
        } catch (Exception exception) {
            System.out.println("Unable to parse payload");
            exception.printStackTrace();
            return new ResponseEntity<Object>(HttpStatus.FORBIDDEN);
        }
        System.out.println("Yup person was logged in: " + account.toString());;
        return new ResponseEntity<Object>(account, HttpStatus.ACCEPTED);
    }

    @RequestMapping("/logout")
    public ResponseEntity<?> logout(RequestEntity entity) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", "csspec_org=");
        return new ResponseEntity<Object>(headers, HttpStatus.OK);
    }
}
