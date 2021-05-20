package svosin.biab.services;

import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import svosin.biab.entities.LoanAccount;
import svosin.biab.entities.LoanRequest;
import svosin.biab.entities.Profile;
import svosin.biab.enums.JobRiskLevel;
import svosin.biab.repos.LoanAccountRepository;
import svosin.biab.repos.LoanRequestRepository;

import java.math.RoundingMode;
import java.util.Date;

@Service
public class LoansService {
    @Autowired
    LoanAccountRepository loanAccountRepository;
    @Autowired
    LoanRequestRepository loanRequestRepository;

    double interestRate = 5.0;

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

    public LoanRequest createLoanRequest(
            Profile requesterProfile,
            Money requestedSum,
            Integer requestedTerm,
            Boolean isFemale,
            Integer age,
            Integer yearsOfLivingInASinglePlace,
            JobRiskLevel jobRiskLevel,
            Boolean hasRealty,
            Boolean hasBankAccounts,
            Boolean hasInsurance,
            Boolean isWorkingInSocField,
            Integer workExperience,
            Money workIncome
    ) {
        return loanRequestRepository.save(
                new LoanRequest(
                        requesterProfile,
                        requestedSum,
                        requestedTerm,
                        isFemale,
                        age,
                        yearsOfLivingInASinglePlace,
                        jobRiskLevel,
                        hasRealty,
                        hasBankAccounts,
                        hasInsurance,
                        isWorkingInSocField,
                        workExperience,
                        workIncome
                )
        );
    }

    public Boolean assessLoanRequest(LoanRequest request) {
        double currentRating = 0.0;
        if(request.getIsFemale()) currentRating += 0.4;
        Integer age = request.getAge();
        double ageFactor = age > 20? (age - 20)*0.1 : 0;
        currentRating += Math.min(ageFactor, 0.3);
        Integer living = request.getYearsOfLivingInASinglePlace();
        double livingFactor = living > 10? 0.42: living * 0.042;
        currentRating += livingFactor;
        switch (request.getJobRiskLevel()) {
            case JOBRISK_LOW: currentRating += 0.55;
            case JOBRISK_MEDIUM: currentRating += 0.16;
        }
        if(request.getHasBankAccounts()) currentRating += 0.45;
        if(request.getHasRealty()) currentRating += 0.35;
        if(request.getHasInsurance()) currentRating += 0.19;
        if(request.getIsWorkingInSocField()) currentRating += 0.21;
        currentRating += request.getWorkExperience() * 0.059;

        Money allowedSum = request.getWorkIncome().dividedBy(
                (( 1 + (request.getRequestedTerm() + 1 ) * interestRate ) / (2*12*100)),
                RoundingMode.UNNECESSARY ); // needs fixup that would factor the interest rate in


        if( currentRating >= 1.25 && allowedSum.isLessThan(request.getRequestedSum()) )
            request.setIsApproved(true);
        loanRequestRepository.save(request);
        return request.getIsApproved();
    }

}
