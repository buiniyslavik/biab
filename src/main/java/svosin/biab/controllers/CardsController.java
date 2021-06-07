package svosin.biab.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import svosin.biab.entities.*;
import svosin.biab.interfaces.MoneyDebitProvider;
import svosin.biab.services.CardsService;
import svosin.biab.services.CheckingAccountService;
import svosin.biab.services.UserService;

import java.lang.reflect.InvocationTargetException;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping(path = "/cards")
public class CardsController {
    @Autowired
    UserService userService;
    @Autowired
    CardsService cardsService;
    @Autowired
    CheckingAccountService checkingAccountService;

    @GetMapping("/all")
    public String showAllCards(Model model, Principal principal) {
        Profile currentUser = userService.findByUsername(principal.getName());
        model.addAttribute("currUser", currentUser);
        List<Card> cards = cardsService.getProfileCardList(currentUser);

        model.addAttribute("cardList", cards);
        return "cards";

    }

    @GetMapping("/new")
    public String prepareForCardIssue(Model model, Principal principal) {

        List<CheckingAccount> userAccounts = checkingAccountService.getCheckingAccountsOfProfile(
                userService.findByUsername(principal.getName())
        );
        model.addAttribute("accounts", userAccounts);
        model.addAttribute("draft", new CardIssueDto());
        return "newcard";
    }

    @PostMapping("/new")
    public String createCheckingAccount(Principal principal, Model model, @ModelAttribute("draft") CardIssueDto draftDTO) {
        Profile currentUser = userService.findByUsername(principal.getName());
        cardsService.createCard(
                checkingAccountService.getById(draftDTO.getAccountToUseId()),
                currentUser
        );
        //return "checkingAccounts";
        //return showAllCheckingAccounts(model, principal);
        return "redirect:all";
    }
}
