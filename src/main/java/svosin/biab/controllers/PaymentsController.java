package svosin.biab.controllers;

import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import svosin.biab.entities.CheckingAccount;
import svosin.biab.entities.PaymentDraftDTO;
import svosin.biab.entities.Profile;
import svosin.biab.entities.Ruble;
import svosin.biab.interfaces.MoneyCreditProvider;
import svosin.biab.interfaces.MoneyDebitProvider;
import svosin.biab.services.CheckingAccountService;
import svosin.biab.services.PaymentProcessingService;
import svosin.biab.services.UserService;

import java.lang.reflect.InvocationTargetException;
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

    @GetMapping("/to/{providerName}/prepare")
    public String payToProviderStepOne(Model model, Principal principal, @PathVariable String providerName)
            throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException {

        MoneyDebitProvider provider = (MoneyDebitProvider) Class
                .forName(providerName)
                .getDeclaredConstructor()
                .newInstance();
        model.addAttribute("provider", provider);

        PaymentDraftDTO paymentDraftDTO = new PaymentDraftDTO();
        paymentDraftDTO.setProvider(provider.getClass().getName());
        model.addAttribute("draft", paymentDraftDTO);
        return "paymentStepOne";
    }

    @PostMapping("/to/{providerName}/confirm")
    public String payToProviderStepTwo(
            Model model,
            Principal principal,
            @PathVariable String providerName,
            @ModelAttribute("draft") PaymentDraftDTO draftDTO
    ) {
        String rawAmount = draftDTO.getAmount();
        Money amount = Money.parse("RUB " + rawAmount);
        Money fee = draftDTO.getProvider().getFeeForAmount(Money.parse("RUB " + rawAmount));
        draftDTO.setFee(
                draftDTO.getProvider().getFeeForAmount(
                        Money.parse("RUB " + rawAmount)
                ).toString()
        );
        String total = amount.plus(fee).toString();
        draftDTO.setTotal(total);
        model.addAttribute("draft", draftDTO);
        return "paymentStepTwo";
    }

}
