package svosin.biab.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import svosin.biab.entities.CheckingAccount;
import svosin.biab.entities.Profile;
import svosin.biab.interfaces.MoneyDebitProvider;
import svosin.biab.services.CheckingAccountService;
import svosin.biab.services.PaymentProcessingService;
import svosin.biab.services.UserService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping(path = "/pay")
public class PaymentsController {
    @Autowired
    CheckingAccountService checkingAccountService;

    @Autowired
    UserService userService;

    @Autowired
    PaymentProcessingService paymentProcessingService;

    @GetMapping("/all")
    public String showAllPaymentProviders(Model model, Principal principal) {
        Profile currentUser = userService.findByUsername(principal.getName());
        model.addAttribute("currUser", currentUser);

        List<MoneyDebitProvider> providers = paymentProcessingService.getAllDebitProviders();

        model.addAttribute("providers", providers);
        return "paymentProviders";

    }

}
