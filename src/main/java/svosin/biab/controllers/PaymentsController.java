package svosin.biab.controllers;

import lombok.SneakyThrows;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import svosin.biab.entities.CheckingAccount;
import svosin.biab.entities.PaymentDraftDTO;
import svosin.biab.entities.PaymentRequest;
import svosin.biab.entities.Profile;
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
    private CheckingAccountService checkingAccountService;

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentProcessingService paymentProcessingService;

    @Autowired
    private AutowireCapableBeanFactory autowireCapableBeanFactory;

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

        List<CheckingAccount> userAccounts = checkingAccountService.getCheckingAccountsOfProfile(
                userService.findByUsername(principal.getName())
        );
        model.addAttribute("accounts", userAccounts);

        PaymentDraftDTO paymentDraftDTO = new PaymentDraftDTO();
  //      paymentDraftDTO.setProvider(provider.getName());
        model.addAttribute("draft", paymentDraftDTO);

        return "paymentStepOne";
    }

    @PostMapping("/to/{providerName}/confirm")
    @SneakyThrows
    public String payToProviderStepTwo(
            Model model,
            Principal principal,
            @PathVariable String providerName,
            @ModelAttribute("draft") PaymentDraftDTO draftDTO
    ) {
        String rawAmount = draftDTO.getAmount();
        MoneyDebitProvider provider = (MoneyDebitProvider) Class.forName(providerName).getDeclaredConstructor().newInstance();
        Money amount = Money.parse("RUB " + rawAmount);
        Money fee = provider.getFeeForAmount(Money.parse("RUB " + rawAmount));
        draftDTO.setFee(
                provider.getFeeForAmount(
                        Money.parse("RUB " + rawAmount)
                ).toString()
        );
        String total = amount.plus(fee).toString();
        draftDTO.setTotal(total);
        draftDTO.setProvider(provider.getName());
        draftDTO.setAccountToUseBalance(checkingAccountService.getBalanceById(draftDTO.getAccountToUseId()));
        model.addAttribute("draft", draftDTO);
        model.addAttribute("payCommand", new PaymentDraftDTO());
        return "paymentStepTwo";
    }

    @PostMapping("/to/{providerName}/process")
//    @SneakyThrows
    public String payToProviderStepThree(
            Model model,
            Principal principal,
            @PathVariable String providerName,
            @ModelAttribute("payCommand") PaymentRequest cmd
    ) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, SecurityException {
        MoneyDebitProvider provider = (MoneyDebitProvider) Class
                .forName(providerName)
                .getDeclaredConstructor()
                .newInstance();
        autowireCapableBeanFactory.autowireBean(provider);
        model.addAttribute("provider", provider);

        CheckingAccount account = checkingAccountService.getById(cmd.getAccountToUseId());

        if(!(account.getOwner().equals(userService.findByUsername(principal.getName())))) {
            throw new SecurityException("Unauthorized spending attempt");
        }

        if(paymentProcessingService.payToProvider(
                provider,
                account,
                Money.parse("RUB "+ cmd.getAmount()),
                cmd.getData()
        )) return "redirect:/start";
        else return "fail";
    }

}
