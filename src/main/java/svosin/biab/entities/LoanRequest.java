package svosin.biab.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.Money;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import svosin.biab.enums.JobRiskLevel;

import java.util.Date;

@Data
@Document
@NoArgsConstructor
public class LoanRequest {
    @Id
    String id;
    Profile requesterProfile;
    Date requestDate;
    Integer requestedSum;
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
    Integer workIncome;

    Double extCorrectionFactor = 0.0;

    private final Double passRate = 1.25;
    Boolean isApproved = false;

    public LoanRequest(
            Profile requesterProfile,
            Integer requestedSum,
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
            Integer workIncome
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
    public LoanRequest(LoanRequestInfo lri, Profile owner) {
        this.requesterProfile = owner;
        this.requestedSum = lri.requestedSum;
        this.requestedTerm = lri.requestedTerm;
        this.isFemale = lri.gender.equals("Ж");
        this.age = lri.age;
        this.yearsOfLivingInASinglePlace = lri.yearsOfLivingInASinglePlace;
        this.jobRiskLevel = lri.jobRiskLevel;
        this.hasRealty = lri.hasRealty;
        this.hasBankAccounts = lri.hasBankAccounts;
        this.hasInsurance = lri.hasInsurance;
        this.isWorkingInSocField = lri.isWorkingInSocField;
        this.workExperience = lri.workExperience;
        this.workIncome = lri.workIncome;
    }
}
