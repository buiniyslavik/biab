package svosin.biab.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class rootController {
    @GetMapping("/start")
    public String showMainPage(Model model, Principal principal) {
        model.addAttribute("currUser", principal.getName());
        return "main";
    }
}
