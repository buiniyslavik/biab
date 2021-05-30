package svosin.biab.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import svosin.biab.repos.ProfileRepository;
import svosin.biab.services.CheckingAccountService;
import svosin.biab.services.PaymentLogService;
import svosin.biab.services.UserService;

import java.security.Principal;

@RequestMapping("/log")
@Controller
public class LogController {
    @Autowired
    UserService userService;

    @Autowired
    PaymentLogService paymentLogService;

    @Autowired
    CheckingAccountService checkingAccountService;

    @RequestMapping("/all")
    public String getUserLogs(Model model, Principal principal) {
        return "logs";
    }
}
