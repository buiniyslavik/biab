package svosin.biab.services;

import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import svosin.biab.entities.CheckingAccount;
import svosin.biab.entities.Profile;
import svosin.biab.exceptions.OutOfFundsException;
import svosin.biab.repos.CheckingAccountRepository;

import java.util.List;

@Service
public class CheckingAccountService {
    @Autowired
    CheckingAccountRepository checkingAccountRepository;

    public List<CheckingAccount> getCheckingAccountsOfProfile(Profile owner) {
        return checkingAccountRepository.findAllByOwner(owner);
    }

    public CheckingAccount createCheckingAccount(Profile owner) {
        return checkingAccountRepository.save(new CheckingAccount(owner));
    }

    public Money debitCheckingAccount(String accountId, Money amount) throws OutOfFundsException {
        CheckingAccount acc = checkingAccountRepository.findById(accountId).orElseThrow();
        Money bal = acc.getCurrentBalance();
        if(bal.isLessThan(amount)) throw new OutOfFundsException("Not enough funds to credit account");
        bal.minus(amount);
        checkingAccountRepository.save(acc);
        return acc.getCurrentBalance();
    }
    public Money creditCheckingAccount(String accountId, Money amount)  {
        CheckingAccount acc = checkingAccountRepository.findById(accountId).orElseThrow();
        Money bal = acc.getCurrentBalance();
        bal.plus(amount);
        checkingAccountRepository.save(acc);
        return acc.getCurrentBalance();
    }
}
