package svosin.biab.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import svosin.biab.services.UserService;

import java.security.Principal;

@Controller
public class rootController {
    @Autowired
    UserService userService;

    @GetMapping("/start")
    public String showMainPage(Model model, Principal principal) {
        model.addAttribute("currUser", userService.findByUsername(principal.getName()).getFullname());
        return "main";
    }
}
