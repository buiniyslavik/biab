package svosin.biab.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import svosin.biab.entities.LoanAccount;
import svosin.biab.entities.LoanRequest;
import svosin.biab.entities.Profile;

import svosin.biab.services.LoansService;
import svosin.biab.services.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping(path = "/loans")
public class LoanAccountsController {
    @Autowired
    LoansService loanAccountService;
    @Autowired
    UserService userService;

    @GetMapping("/all")
    public String showAllLoans(Model model, Principal principal) {
        Profile currentUser = userService.findByUsername(principal.getName());
        model.addAttribute("currUser", currentUser);
        List<LoanAccount> loanAccounts = loanAccountService.getLoanAccountsOfProfile(currentUser);

        model.addAttribute("lAccounts", loanAccounts);
        return "loans";

    }

    @GetMapping("/new")
    public String showLoanRequestPage() {
        return "loanRequest";
    }

    @PostMapping("/new")
    public String processLoanRequest(@ModelAttribute("req") @Valid LoanRequest loanRequest, Principal principal) {
        loanRequest.setRequesterProfile(userService.findByUsername(principal.getName()));
        loanAccountService.assessLoanRequest(loanRequest);
        return "loans";
    }

}
