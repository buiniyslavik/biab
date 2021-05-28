package svosin.biab.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import svosin.biab.entities.CheckingAccount;
import svosin.biab.entities.Profile;
import svosin.biab.services.CheckingAccountService;
import svosin.biab.services.UserService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping(path = "/checking")
public class CheckingAccountsController {
    @Autowired
    CheckingAccountService checkingAccountService;
    @Autowired
    UserService userService;

    @GetMapping("/all")
    public String showAllCheckingAccounts(WebRequest request, Model model, Principal principal) {
        Profile currentUser = userService.findByUsername(principal.getName());
        model.addAttribute("currUser", currentUser);
        List<CheckingAccount> checkingAccounts = checkingAccountService.getCheckingAccountsOfProfile(currentUser);

        model.addAttribute("chkAccounts", checkingAccounts);
        return "checkingAccounts";

    }

    @PostMapping("/new")
    public String createCheckingAccount(Principal principal) {
        Profile currentUser = userService.findByUsername(principal.getName());
        checkingAccountService.createCheckingAccount(currentUser);
        return "checkingAccounts";
    }

}
