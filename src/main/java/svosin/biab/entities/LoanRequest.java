package svosin.biab.entities;

import lombok.Data;
import org.joda.money.Money;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import svosin.biab.enums.JobRiskLevel;

import java.util.Date;

@Data
@Document
public class LoanRequest {
    @Id
    String id;
    Profile requesterProfile;
    Date requestDate;
    Money requestedSum;
    Integer requestedTerm; // months
    /*
    Быкова Н.Н. Основные методы анализа кредитоспособности заёмщика //
    Гуманитарные научные исследования. 2017. № 2 [Электронный ресурс].
    URL: https://human.snauka.ru/2017/02/21757 (дата обращения: 17.05.2021).
     */
    Boolean isFemale;
    Integer age;
    Integer yearsOfLivingInASinglePlace;
    JobRiskLevel jobRiskLevel;
    Boolean hasRealty, hasBankAccounts, hasInsurance;
    Boolean isWorkingInSocField;
    Integer workExperience;
    Money workIncome;

    Double extCorrectionFactor = 0.0;

    private final Double passRate = 1.25;
    Boolean isApproved = false;

    public LoanRequest(
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
        this.requesterProfile = requesterProfile;
        this.requestedSum = requestedSum;
        this.requestedTerm = requestedTerm;
        this.isFemale = isFemale;
        this.age = age;
        this.yearsOfLivingInASinglePlace = yearsOfLivingInASinglePlace;
        this.jobRiskLevel = jobRiskLevel;
        this.hasRealty = hasRealty;
        this.hasBankAccounts = hasBankAccounts;
        this.hasInsurance = hasInsurance;
        this.isWorkingInSocField = isWorkingInSocField;
        this.workExperience = workExperience;
        this.workIncome = workIncome;
    }
}
