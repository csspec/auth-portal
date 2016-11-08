package org.csspec.auth.mvc;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.csspec.auth.Configuration;
import org.csspec.auth.api.RequestApproval;
import org.csspec.auth.db.schema.Account;
import org.csspec.auth.db.schema.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    private RequestApproval requestApproval;

    @Autowired
    public LoginController(RequestApproval requestApproval) {
        this.requestApproval = requestApproval;
    }


    @RequestMapping("/home")
    public String home(Model model, HttpServletRequest request) {
        try {
            Account account = requestApproval.approveRequest(request, UserRole.ADMIN);
            System.out.println("Admin was logged in");
        } catch (Exception e) {
            System.out.println(e);
            return "index";
        }
        return "admin_template";
    }

    @RequestMapping("/admin")
    public String adminPage(Model model, HttpServletRequest request) {
        return "admin_template";
    }

    @RequestMapping(value = {"/login", "/", "/services/login" })
    public String login(Model model) {
        return "login";
    }

    @RequestMapping("/services/logout")
    @ResponseStatus(HttpStatus.MOVED_PERMANENTLY)
    public ModelAndView logout(@CookieValue(value = Configuration.CSS_ORG_COOKIE_NAME) String cookieValue,
                @RequestParam(value = "redirect_uri", required = false) String redirect_uri, HttpServletResponse response) {
        Cookie cookie = new Cookie(Configuration.CSS_ORG_COOKIE_NAME, "");
        cookie.setPath("/");
        if (redirect_uri != null && !redirect_uri.equals("")) {
            RedirectView redirectView = new RedirectView(redirect_uri, true);
            response.addCookie(cookie);
            return new ModelAndView(redirectView);
        }

        response.addCookie(cookie);
        RedirectView redirectView = new RedirectView("/login");
        return new ModelAndView(redirectView);
    }
}
