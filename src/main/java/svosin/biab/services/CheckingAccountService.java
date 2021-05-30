package svosin.biab.services;

import lombok.extern.slf4j.Slf4j;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import svosin.biab.entities.CheckingAccount;
import svosin.biab.entities.Profile;
import svosin.biab.enums.LogOperationType;
import svosin.biab.exceptions.OutOfFundsException;
import svosin.biab.persistEntities.PersistCheckingAccount;
import svosin.biab.repos.CheckingAccountRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CheckingAccountService {
    @Autowired
    CheckingAccountRepository checkingAccountRepository;

    @Autowired
    PaymentLogService paymentLogService;
    public List<CheckingAccount> getCheckingAccountsOfProfile(Profile owner) {
        var persistForm = checkingAccountRepository.findAllByOwner(owner);
        List<CheckingAccount> ca = new ArrayList<>();
        persistForm.forEach(e -> ca.add(new CheckingAccount(e)));
        return ca;
    }

    public PersistCheckingAccount createCheckingAccount(Profile owner) {
        return checkingAccountRepository.save(new CheckingAccount(owner).toPersist());
    }

    public Money debitCheckingAccount(String accountId, Money amount, String message) throws OutOfFundsException {
        CheckingAccount acc = new CheckingAccount(checkingAccountRepository.findById(accountId).orElseThrow());
        Money bal = acc.getCurrentBalance();
        if(bal.isLessThan(amount)) throw new OutOfFundsException("Not enough funds to credit account");
        acc.setCurrentBalance(bal.minus(amount));
        checkingAccountRepository.save(acc.toPersist());
        paymentLogService.logPayment(
               checkingAccountRepository.findById(accountId).orElseThrow().getOwner().getUserId(),
                accountId,
                LogOperationType.DEBIT,
                message,
                amount.toString()
        );

        log.info("debited account " + accountId + " for " + amount.toString());
        return acc.getCurrentBalance();
    }
    public Money creditCheckingAccount(String accountId, Money amount)  {
        CheckingAccount acc = new CheckingAccount(checkingAccountRepository.findById(accountId).orElseThrow());
        Money bal = acc.getCurrentBalance();
        acc.setCurrentBalance(bal.plus(amount));
        checkingAccountRepository.save(acc.toPersist());
        return acc.getCurrentBalance();
    }
}
