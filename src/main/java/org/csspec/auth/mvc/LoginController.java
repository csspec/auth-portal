package org.csspec.auth.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/home")
public class LoginController {

    @RequestMapping(method = RequestMethod.GET)
    public String home(Model model) {
        return "index";
    }
}
