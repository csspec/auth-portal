package org.csspec.auth.api;


import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.csspec.auth.exceptions.EmailAlreadyExistsException;
import org.csspec.auth.exceptions.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;

@RestController
@RequestMapping("/auth/googlesignin/verify")
public class GoogleAccountVerifier {
    private final String CLIENT_ID = "";
    private GoogleIdTokenVerifier verifier;

    public static Set<String> verifiedEmails = new HashSet<>();

    public GoogleAccountVerifier() throws IOException, GeneralSecurityException {
        this.verifier = new GoogleIdTokenVerifier.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance())
                .setAudience(Arrays.asList(CLIENT_ID))
                .setIssuer("accounts.google.com")
                .build();
    }

    public static class OAuthGoogleToken {
        private String token;

        public OAuthGoogleToken(String token) {
            this.token = token;
        }

        public OAuthGoogleToken() { }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

    public boolean checkIfInDatabase(String email) {
        return false;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> verifyEmailId(@RequestBody OAuthGoogleToken token) throws IOException,
            GeneralSecurityException, EmailAlreadyExistsException {
        GoogleIdToken idToken = verifier.verify(token.getToken());
        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();

            // Print user identifier
            String userId = payload.getSubject();

            String email = payload.getEmail();
            boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());

            System.out.println("Google User ID: " + userId + ", GMail: " + email + ", Verified: " + emailVerified);

            if (!emailVerified) {
                throw new GeneralSecurityException("email id <" + email + "> not verified");
            } else if (checkIfInDatabase(email)) {
                throw new EmailAlreadyExistsException(email);
            }
            verifiedEmails.add(email);
            Map<String, Boolean> response = new HashMap<>();
            response.put("verified", true);
            return new ResponseEntity<Object>(response, HttpStatus.OK);
        } else {
            System.out.println("Invalid ID token was received (token: " + token.getToken() + ").");
            throw new GeneralSecurityException("Invalid ID token was received (token: " + token.getToken() + ").");
        }
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse emailAlreadyExistsExceptionHandler(EmailAlreadyExistsException e) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.toString());
    }
}
