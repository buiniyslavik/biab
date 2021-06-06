package svosin.biab.entities;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.Money;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import svosin.biab.persistEntities.PersistLoanAccount;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

@Data
@Document
@Slf4j
public class LoanAccount {
    @Id
    String id;

    BigDecimal interestRate;
    LocalDate openingDate;
    LocalDate expectedCloseDate;
    LocalDate nextPaymentDueDate;
    Money latePaymentPenalty = Money.zero(Ruble.rub);

    Money initialAmount = Money.zero(Ruble.rub);
    Money repaidAmount = Money.zero(Ruble.rub);
    Money monthlyPayment = Money.zero(Ruble.rub);

    Money accumulatedPenalties = Money.zero(Ruble.rub);
    Money accumulatedInterest = Money.zero(Ruble.rub);
    Boolean paymentWasMissed = true;

    Boolean isReadyForDeletion = false;

    @DBRef
    Profile owner;

    public LoanAccount(Double interestRate, LocalDate expectedCloseDate, Money latePaymentPenalty, Money initialAmount) {
        this.interestRate = BigDecimal.valueOf(interestRate);
        this.openingDate = LocalDate.now();
        this.expectedCloseDate = expectedCloseDate;
        this.latePaymentPenalty = latePaymentPenalty;
        this.initialAmount = initialAmount;
    }

    public LoanAccount(LoanRequest loanRequest) {
        this.openingDate = LocalDate.now();
        this.expectedCloseDate = this.openingDate.plusMonths(loanRequest.getRequestedTerm());
        this.nextPaymentDueDate = this.openingDate.plusMonths(1);
        this.latePaymentPenalty = Money.of(Ruble.rub, 1000);
        this.interestRate = BigDecimal.valueOf(5.0);
        this.initialAmount = Money.of(Ruble.rub, loanRequest.getRequestedSum());
        this.monthlyPayment = initialAmount.dividedBy(new BigDecimal(loanRequest.getRequestedTerm()), RoundingMode.HALF_UP);
        this.paymentWasMissed = false;
    }

    public PersistLoanAccount toPersist() {
        return new PersistLoanAccount(id, interestRate.toString(), openingDate, expectedCloseDate, nextPaymentDueDate,
                latePaymentPenalty.toString(), initialAmount.toString(), repaidAmount.toString(), monthlyPayment.toString(),
                accumulatedPenalties.toString(), accumulatedInterest.toString(), paymentWasMissed, owner);
    }

    public LoanAccount(PersistLoanAccount p) {
        id = p.getId();
        interestRate = new BigDecimal(p.getInterestRate());
        interestRate = interestRate.setScale(5, RoundingMode.HALF_UP);
        openingDate = p.getOpeningDate();
        expectedCloseDate = p.getExpectedCloseDate();
        nextPaymentDueDate = p.getNextPaymentDueDate();
        if (p.getLatePaymentPenalty() != null)
            latePaymentPenalty = Money.parse(p.getLatePaymentPenalty());
        else latePaymentPenalty = Money.zero(Ruble.rub);

        if (p.getInitialAmount() != null)
            initialAmount = Money.parse(p.getInitialAmount());
        else initialAmount = Money.zero(Ruble.rub);

        if (p.getRepaidAmount() != null)
            repaidAmount = Money.parse(p.getRepaidAmount());
        else repaidAmount = Money.zero(Ruble.rub);

        if (p.getMonthlyPayment() != null)
            monthlyPayment = Money.parse(p.getMonthlyPayment());
        else monthlyPayment = Money.zero(Ruble.rub);

        if (p.getAccumulatedPenalties() != null)
            accumulatedPenalties = Money.parse(p.getAccumulatedPenalties());
        else accumulatedPenalties = Money.zero(Ruble.rub);

        if (p.getAccumulatedInterest() != null)
            accumulatedInterest = Money.parse(p.getAccumulatedInterest());
        else accumulatedInterest = Money.zero(Ruble.rub);

        paymentWasMissed = p.getPaymentWasMissed();

        owner = p.getOwner();
    }

    public void processInterest() {
        log.info("loan " + id + ": processing interest");
        if (paymentWasMissed) {
            accumulatedPenalties = accumulatedPenalties.plus(latePaymentPenalty);
            log.info("loan " + id + ": penalty incurred");
        }
        Money remainingAmount = initialAmount.plus(accumulatedInterest.plus(accumulatedPenalties)).minus(repaidAmount);
        log.info("remainingAmount " + remainingAmount.toString());
        log.info("irate " + interestRate.toString());
        Money currentInterest = remainingAmount.multipliedBy(
                (
                        interestRate.divide(
                                new BigDecimal("100.000"), RoundingMode.HALF_UP
                        ).divide(
                                new BigDecimal("365.000"), RoundingMode.HALF_UP
                        ).multiply(
                                new BigDecimal(LocalDate.now().lengthOfMonth())
                        )
                ), RoundingMode.HALF_UP
        );

        log.info(interestRate.divide(
                new BigDecimal("100.0"), RoundingMode.HALF_UP
        ).toString())

        ;

        accumulatedInterest = accumulatedInterest.plus(currentInterest);
        log.info("ci " + currentInterest.toString());
        log.info("ai " + accumulatedInterest.toString());
        nextPaymentDueDate = nextPaymentDueDate.plusMonths(1);
        paymentWasMissed = true;
    }

    public void processPayment(Money amount) {
        log.info("loan " + id + ": processing payment");
        repaidAmount = repaidAmount.plus(amount);
        if(monthlyPayment.isLessThan(amount) || monthlyPayment.isEqual(amount)) {
            paymentWasMissed = false;
        }
        if(repaidAmount.isGreaterThan(initialAmount.plus(accumulatedPenalties).plus(accumulatedInterest)))
                isReadyForDeletion = true;

    }

    public Money getOutstandingAmount() {
        return initialAmount.plus(accumulatedPenalties).plus(accumulatedInterest);
    }

}
