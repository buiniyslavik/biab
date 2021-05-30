package svosin.biab.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import svosin.biab.entities.PaymentLogEntry;
import svosin.biab.enums.LogOperationType;
import svosin.biab.repos.LogRepository;

import java.util.Date;

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
        return logRepository.save(entry);
    }
}
