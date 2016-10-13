package org.csspec.auth.mvc;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.csspec.auth.db.repositories.ClientApplicationRepository;
import org.csspec.auth.db.schema.ClientApplication;
import org.csspec.auth.db.services.ClientApplicationDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.Set;

/**
 * All the UI needed for accessing the OAuth token
 *
 * <h1>Web flow for applications using OAuth services</h1>
 *
 * <ol>
 *     <li>
 *         Application first redirects user to {@code /oauth/authorize} with following parameters
 *          <ul>
 *              <li><code>client_id</code>: Client ID issued to application <strong>REQUIRED</strong></li>
 *              <li><code>redirect_uri</code>: Redirect URI to which users will be sent after Authorization</li>
 *          </ul>
 *     </li>
 *     <li>
 *         If user successfully signs in and accept the application's request, then the auth-portal will redirect
 *         to your site with parameter {@code token}.
 *     </li>
 *     <li>
 *         Client will exchange the above {@code token} for {@code access_token} by following request
 *         <br />
 *         <code>
 *             POST /oauth/access_token
 *         </code>
 *     </li>
 * </ol>
 */
@Controller
@RequestMapping("/oauth")
public class OAuthMvcController {
    private ClientApplicationDetailService service;

    @Autowired
    private OAuthMvcController(ClientApplicationDetailService service) {
        this.service = service;
    }

    @RequestMapping("/authorize")
    public String authorize(Model model, @RequestParam(name = "redirect_uri", required = false) String redirect_uri,
                            @RequestParam(name="client_id", required = true) String client_id,
                            @RequestParam(name="response_type", defaultValue = "code") String response_type,
                            HttpServletRequest request,
                            HttpServletResponse response) {
        String registeredRedirectUri = null;
        model.addAttribute("client_id", client_id);
        try {
            ClientApplication application = service.loadClientByClientId(client_id);
            if (redirect_uri == null) {
                Set<String> uris = application.getRegisteredRedirectUri();
                assert uris != null && uris.size() != 0;
                Iterator<String> stringIterator= uris.iterator();
                if (stringIterator.hasNext()) {
                    registeredRedirectUri = stringIterator.next();
                }
            } else {
                registeredRedirectUri = redirect_uri;
            }
        } catch (Exception e) {
            System.out.println("Unknown client tried to login client_id=" + client_id);
            return "unknown_client";
        }
        model.addAttribute("redirect_to", registeredRedirectUri);
        model.addAttribute("response_type", response_type);
        return "login";
    }
}
