package svosin.biab.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import svosin.biab.entities.CheckingAccount;
import svosin.biab.entities.PaymentLogEntry;
import svosin.biab.entities.Profile;
import svosin.biab.enums.LogOperationType;
import svosin.biab.repos.LogRepository;

import java.util.Calendar;
import java.util.List;

@Service
public class PaymentLogService {
    @Autowired
    LogRepository logRepository;

    public PaymentLogEntry logPayment(
            String userId,
            String accountId,
            LogOperationType operationType,
            String amount,
            String paymentData
    ) {
        PaymentLogEntry entry = new PaymentLogEntry(userId, accountId, operationType, amount, paymentData);
        entry.setDate(Calendar.getInstance().getTime());
        return logRepository.save(entry);
    }

    public List<PaymentLogEntry> getProfileLogs(Profile profile) {
        return logRepository.getAllByUserId(profile.getUserId(), Pageable.unpaged());
    }

    public List<PaymentLogEntry> getCheckingAccountLogs(CheckingAccount checkingAccount) {
        return logRepository.getAllByAccountId(checkingAccount.getId(), Pageable.unpaged());
    }

}
