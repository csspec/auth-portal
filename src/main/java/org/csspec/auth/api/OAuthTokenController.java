package org.csspec.auth.api;

import org.csspec.auth.URIQueryParser;
import org.csspec.auth.config.JwtConfiguration;
import org.csspec.auth.db.schema.ClientApplication;
import org.csspec.auth.db.services.ClientApplicationDetailService;
import org.csspec.auth.db.services.CodeIssuer;
import org.csspec.auth.exceptions.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @Autowired
    public OAuthTokenController(ClientApplicationDetailService clientService, CodeIssuer codeIssuer) {
        this.clientService = clientService;
        this.codeIssuer = codeIssuer;
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
    public ResponseEntity<?> issueCode(RequestEntity<?> entity) {
        ClientApplication application = validateAndGetClient(entity, false, false);
        if (application == null)
            return new ResponseEntity<>(new ErrorResponse(403, "Bad credentials"), HttpStatus.FORBIDDEN);

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
    public ResponseEntity<?> getAccessToken(RequestEntity<?> entity) {
        ClientApplication application = validateAndGetClient(entity, true, true);
        if (application == null) {
            return new ResponseEntity<>(new ErrorResponse(403, "Bad credentials"), HttpStatus.FORBIDDEN);
        }
        final StringBuilder scope = new StringBuilder();
        application.getAllowedServices().forEach(name -> scope.append(name + " "));

        String jwt = JwtConfiguration.getJwt(application.getClientId(), scope.toString());
        Map<String, Object> map = new HashMap<>();
        map.put("access_token", jwt);

        // TODO::= we have to add the expiry time of the token also.
        return new ResponseEntity<Object>(map, HttpStatus.OK);
    }

    private String parseAuthorizationHeader(RequestEntity<?> entity) {
        List<String> auths = entity.getHeaders().get("Authorization");
        if (auths == null)
            return null;

        String authToken = auths.get(0);
        if (!authToken.trim().startsWith("Bearer "))
            return null;

        String []strings = authToken.split(" ");
        if (strings.length <= 1)
            return null;

        return strings[1];
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
     * @param entity
     * @return
     */
    @RequestMapping("/check_token")
    public ResponseEntity<?> checkAccessToken(RequestEntity<?> entity) {
        ClientApplication application = validateAndGetClient(entity, true, false);
        if (application == null) {
            return new ResponseEntity<>(new ErrorResponse(403, "Bad credentials"), HttpStatus.FORBIDDEN);
        }

        String jwt = parseAuthorizationHeader(entity);
        if (jwt == null)
            return new ResponseEntity<>(new ErrorResponse(403, "Bad credentials"), HttpStatus.FORBIDDEN);
        Map<String, Object> claims;
        try {
             claims = JwtConfiguration.getClaims(jwt);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse(403, "Bad authorization header"), HttpStatus.FORBIDDEN);
        }

        Map<String, Object> details = new HashMap<>();
        String claimedId = (String)claims.get("sub");

        if (claimedId == null || !application.getClientId().equals(claimedId))
            return new ResponseEntity<>(new ErrorResponse(403, "Bad client"), HttpStatus.FORBIDDEN);

        return new ResponseEntity<>(application, HttpStatus.OK);
    }
}
