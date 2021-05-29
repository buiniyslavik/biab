package svosin.biab.services;

import debitProviders.NullDebitProvider;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import svosin.biab.creditProviders.NullCreditProvider;
import svosin.biab.entities.CheckingAccount;
import svosin.biab.entities.Profile;
import svosin.biab.interfaces.MoneyCreditProvider;
import svosin.biab.interfaces.MoneyDebitProvider;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class PaymentProcessingService {
    @Autowired
    CheckingAccountService checkingAccountService;

    public List<MoneyCreditProvider> getAllCreditProviders() {
        return Stream.of(
                new NullCreditProvider()
        ).collect(Collectors.toList());
    }

    public List<MoneyDebitProvider> getAllDebitProviders() {
        return Stream.of(
                new NullDebitProvider()
        ).collect(Collectors.toList());
    }

    public Money getProviderFeeForAmount(MoneyDebitProvider provider, Money amount) {
        return provider.getFeeForAmount(amount);
    }

    public Boolean payToProvider(MoneyDebitProvider provider, CheckingAccount payer, Money amount) {
        Money fee = provider.getFeeForAmount(amount);

    }

}
