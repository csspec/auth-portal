package org.csspec.auth.mvc;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.csspec.auth.api.RequestApproval;
import org.csspec.auth.db.schema.Account;
import org.csspec.auth.db.schema.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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

    @RequestMapping("/login")
    public String login(Model model) {
        return "login";
    }
}
