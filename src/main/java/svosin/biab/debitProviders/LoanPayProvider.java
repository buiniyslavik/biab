package svosin.biab.debitProviders;

import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import svosin.biab.entities.LoanAccount;
import svosin.biab.entities.Ruble;
import svosin.biab.exceptions.PaymentProcessingException;
import svosin.biab.interfaces.MoneyDebitProvider;
import svosin.biab.services.LoansService;

@Component
public class LoanPayProvider implements MoneyDebitProvider {

    @Autowired
    LoansService loansService;


    @Override
    public String getName() {
        return "Погашение кредита";
    }

    @Override
    public String getFee() {
        return "0%";
    }

    @Override
    public Money getMinAmount() {
        return Money.of(Ruble.rub, 0.01);
    }

    @Override
    public Money getMaxAmount() {
        return Money.of(Ruble.rub, 10000000);
    }

    @Override
    public Money getFeeForAmount(Money amount) {
        return Money.zero(Ruble.rub);
    }

    @Override
    public Boolean processPayment(Money amount, String data) throws PaymentProcessingException {
        try {
            LoanAccount account = loansService.getLoanById(data);
            account.processPayment(amount);
            loansService.refreshLoan(account);
            if (account.getIsReadyForDeletion()) {
                loansService.deleteLoan(data);
            }

            return true;
        }
        catch(Exception e) {
            throw new PaymentProcessingException();
        }
    }
}
