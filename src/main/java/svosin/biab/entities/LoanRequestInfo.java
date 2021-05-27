package svosin.biab.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.Money;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import svosin.biab.enums.JobRiskLevel;

import java.util.Date;

@Data
@NoArgsConstructor
public class LoanRequestInfo {
    Integer requestedSum;
    Integer requestedTerm; // months
    /*
    Быкова Н.Н. Основные методы анализа кредитоспособности заёмщика //
    Гуманитарные научные исследования. 2017. № 2 [Электронный ресурс].
    URL: https://human.snauka.ru/2017/02/21757 (дата обращения: 17.05.2021).
     */
    String gender;
    Integer age;
    Integer yearsOfLivingInASinglePlace;
    JobRiskLevel jobRiskLevel;
    Boolean hasRealty, hasBankAccounts, hasInsurance;
    Boolean isWorkingInSocField;
    Integer workExperience;
    Integer workIncome;
}
