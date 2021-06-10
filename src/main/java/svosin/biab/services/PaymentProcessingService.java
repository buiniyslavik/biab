package svosin.biab.services;

import lombok.extern.slf4j.Slf4j;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import svosin.biab.creditProviders.NullCreditProvider;
import svosin.biab.debitProviders.LoanPayProvider;
import svosin.biab.debitProviders.NullDebitProvider;
import svosin.biab.entities.CheckingAccount;
import svosin.biab.exceptions.OutOfFundsException;
import svosin.biab.exceptions.PaymentProcessingException;
import svosin.biab.interfaces.MoneyCreditProvider;
import svosin.biab.interfaces.MoneyDebitProvider;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class PaymentProcessingService {
    @Autowired
    CheckingAccountService checkingAccountService;

    @Autowired
    LoanPayProvider loanPayProvider;

    public List<MoneyCreditProvider> getAllCreditProviders() {
        return Stream.of(
                new NullCreditProvider()
        ).collect(Collectors.toList());
    }

    public List<MoneyDebitProvider> getAllDebitProviders() {
        return Stream.of(
                new NullDebitProvider(),
                loanPayProvider
        ).collect(Collectors.toList());
    }

    public Money getProviderFeeForAmount(MoneyDebitProvider provider, Money amount) {
        return provider.getFeeForAmount(amount);
    }

    public Boolean payToProvider(MoneyDebitProvider provider, CheckingAccount payer, Money amount, String data) {
        Money fee = provider.getFeeForAmount(amount);
        try {
            checkingAccountService.debitCheckingAccount(payer.getId(), amount.plus(fee),
                    provider.getName() + " / " + amount.toString() + " / " + data);
            provider.processPayment(amount, data);
        }
        catch (OutOfFundsException e) {
            return false;
        }
        catch (PaymentProcessingException e) {
            checkingAccountService.creditCheckingAccount(payer.getId(), amount.plus(fee));
            return false;
        }
        return true;
    }
    public Boolean payCard(String merchant, CheckingAccount payer, Money amount, String data) {
        try {
            checkingAccountService.debitCheckingAccount(payer.getId(), amount,
                    merchant + " / " + amount.toString() + " / " + data);
        }
        catch (OutOfFundsException e) {
            return false;
        }
        /*
        here we would have code that signals the bank to send out the actual money,
        but we're just mock bank, so it's not here
         */
        return true;
    }


    public Boolean fundAccount(MoneyCreditProvider provider, CheckingAccount payee, Money amount) {
        if(provider.processCredit(amount)) {
            checkingAccountService.creditCheckingAccount(payee.getId(), amount);
            return true;
        }
        return false;
    }

}
