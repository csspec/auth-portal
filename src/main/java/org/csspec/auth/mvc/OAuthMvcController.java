package org.csspec.auth.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    @RequestMapping("/authorize")
    public String authorize(Model model, @RequestParam(name = "redirect_uri", defaultValue = "/home") String redirect_uri,
                            @RequestParam(name="client_id", required = true) String client_id) {
        model.addAttribute("redirect_to", redirect_uri);
        model.addAttribute("client_id", client_id);
        return "login";
    }
}
