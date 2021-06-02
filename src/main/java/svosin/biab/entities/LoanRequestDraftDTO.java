package svosin.biab.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import svosin.biab.enums.JobRiskLevel;

import java.util.Date;

@Data
@NoArgsConstructor
public class LoanRequestDraftDTO {
    @Id
    String id;
    Profile requesterProfile;
    Date requestDate;
    Integer requestedSum;
    Integer requestedTerm; // months
    String gender;
    Integer age;
    Integer yearsOfLivingInASinglePlace;
    String jobRiskLevel;
    Boolean hasRealty, hasBankAccounts, hasInsurance;
    Boolean isWorkingInSocField;
    Integer workExperience;
    Integer workIncome;

   // Double extCorrectionFactor = 0.0;

   // private final Double passRate = 1.25;
   // Boolean isApproved = false;
}
