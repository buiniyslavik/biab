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
import svosin.biab.entities.LoanRequestInfo;
import svosin.biab.entities.Profile;

import svosin.biab.enums.JobRiskLevel;
import svosin.biab.services.LoansService;
import svosin.biab.services.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

import static svosin.biab.enums.JobRiskLevel.JOBRISK_HIGH;

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
    public String showLoanRequestPage(Model model) {
        model.addAttribute("req", new LoanRequest());
        return "loanRequest";
    }

    @ModelAttribute("allGenders")
    public String[] getAllGenders() {
        return new String[]{ "М", "Ж" };
    }

    @ModelAttribute("allRiskLevels")
    public String[] getAllRiskLevels() {
        return new String[]{"JOBRISK_HIGH", "JOBRISK_MEDIUM", "JOBRISK_LOW"};
    }

    @PostMapping("/new")
    public String processLoanRequest(@ModelAttribute("req") @Valid LoanRequestInfo loanRequestInfo, Principal principal) {
        LoanRequest lr = new LoanRequest(loanRequestInfo, userService.findByUsername(principal.getName()));
        loanAccountService.assessLoanRequest(lr);
        return "loans";
    }

}
