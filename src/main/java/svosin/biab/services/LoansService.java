package svosin.biab.services;

import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import svosin.biab.entities.LoanAccount;
import svosin.biab.entities.Profile;
import svosin.biab.repos.LoanAccountRepository;
import svosin.biab.repos.LoanRequestRepository;

import java.util.Date;

@Service
public class LoansService {
    @Autowired
    LoanAccountRepository loanAccountRepository;
    @Autowired
    LoanRequestRepository loanRequestRepository;

    public LoanAccount createLoan(
            Double interestRate,
            Date expectedCloseDate,
            Money latePaymentPenalty,
            Money initialAmount
    ) {
        return loanAccountRepository.save(
                new LoanAccount(interestRate, expectedCloseDate, latePaymentPenalty, initialAmount)
        );
    }

    public Iterable<LoanAccount> getLoanAccountsOfProfile(Profile owner) {
        return loanAccountRepository.findAllByOwner(owner);
    }

    public LoanAccount deleteLoan(LoanAccount loanId) {
        loanAccountRepository.delete(loanId);
        return loanId;
    }

    //--------------------


}
