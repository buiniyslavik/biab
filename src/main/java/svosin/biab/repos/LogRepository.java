package svosin.biab.repos;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import svosin.biab.entities.PaymentLogEntry;

import java.util.List;

public interface LogRepository extends MongoRepository<PaymentLogEntry, String> {
    List<PaymentLogEntry> getAllByUserId(String userId, Pageable pageable);
    List<PaymentLogEntry> getAllByAccountId(String accountId, Pageable pageable);
}
