package svosin.biab.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import svosin.biab.entities.LoanAccount;
import svosin.biab.entities.LoanRequest;
import svosin.biab.entities.Profile;
import svosin.biab.enums.JobRiskLevel;
import svosin.biab.enums.LogOperationType;
import svosin.biab.persistEntities.PersistLoanAccount;
import svosin.biab.repos.LoanAccountRepository;
import svosin.biab.repos.LoanRequestRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class LoansService {
    @Autowired
    LoanAccountRepository loanAccountRepository;
    @Autowired
    LoanRequestRepository loanRequestRepository;
    @Autowired
    CheckingAccountService checkingAccountService;
    @Autowired
    PaymentLogService paymentLogService;

    double interestRate = 5.0;

    public List<LoanAccount> getLoanAccountsOfProfile(Profile owner) {
        List<PersistLoanAccount> plist = loanAccountRepository.findAllByOwner(owner);
        List<LoanAccount> loans = new ArrayList<>();
        plist.forEach(p -> {
            loans.add(new LoanAccount(p));
            log.info(p.toString());
        });
        return loans;
    }

    public LoanAccount deleteLoan(LoanAccount loanId) {
        loanAccountRepository.delete(loanId.toPersist());
        return loanId;
    }

    public LoanAccount refreshLoan(LoanAccount loanId) {
        loanAccountRepository.save(loanId.toPersist());
        return loanId;
    }


    public LoanAccount getLoanById(String id) {
        return new LoanAccount(loanAccountRepository.findById(id).orElseThrow());
    }

    public void deleteLoan(String id) {
        loanAccountRepository.deleteById(id);
    }

    public LoanRequest assessLoanRequest(LoanRequest request) {
        double currentRating = 0.0;
        if (request.getIsFemale()) currentRating += 0.4;
        Integer age = request.getAge();
        double ageFactor = age > 20 ? (age - 20) * 0.1 : 0;
        currentRating += Math.min(ageFactor, 0.3);
        Integer living = request.getYearsOfLivingInASinglePlace();
        double livingFactor = living > 10 ? 0.42 : living * 0.042;
        currentRating += livingFactor;
        switch (request.getJobRiskLevel()) {
            case JOBRISK_LOW:
                currentRating += 0.55;
            case JOBRISK_MEDIUM:
                currentRating += 0.16;
        }
        if (request.getHasBankAccounts()) currentRating += 0.45;
        if (request.getHasRealty()) currentRating += 0.35;
        if (request.getHasInsurance()) currentRating += 0.19;
        if (request.getIsWorkingInSocField()) currentRating += 0.21;
        currentRating += request.getWorkExperience() * 0.059;

        Integer allowedSum = Math.toIntExact(Math.round(request.getWorkIncome() /
                ((1 + (request.getRequestedTerm() + 1) * interestRate) / (2 * 12 * 100))));

        if (currentRating >= 1.25 && allowedSum >= request.getRequestedSum())
            request.setIsApproved(true);

        LoanRequest finRequest = loanRequestRepository.save(request);
        log.info("Loan request approval: " + request.getIsApproved().toString() + ", rating is " + currentRating
                + ", allowed sum is " + allowedSum);

        return finRequest;
    }

    public void issueLoan(LoanRequest lr, String caId) {
        LoanAccount loanAccount = new LoanAccount(lr);
        loanAccount.setOwner(lr.getRequesterProfile());
        checkingAccountService.creditCheckingAccount(caId, loanAccount.getInitialAmount());
        paymentLogService.logPayment(loanAccount.getOwner().getUserId(),
                caId,
                LogOperationType.CREDIT,
                loanAccount.getInitialAmount().toString(),
                "Выдача кредита");
        loanAccountRepository.save(loanAccount.toPersist());
    }

    public List<LoanAccount> getAllByDueDate(LocalDate date) {
        var plist = loanAccountRepository.findAllByNextPaymentDueDate(date);
        var acclist = new ArrayList<LoanAccount>();
        plist.forEach(p -> acclist.add(new LoanAccount(p)));
        return acclist;
    }

    public void save(LoanAccount loanAccount)
    {
        loanAccountRepository.save(loanAccount.toPersist());
    }

}
