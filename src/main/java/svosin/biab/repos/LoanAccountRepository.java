package svosin.biab.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import svosin.biab.entities.CheckingAccount;
import svosin.biab.entities.LoanAccount;
import svosin.biab.entities.Profile;
import svosin.biab.persistEntities.PersistLoanAccount;

import java.time.LocalDate;
import java.util.List;

public interface LoanAccountRepository extends MongoRepository<PersistLoanAccount, String> {
    List<PersistLoanAccount> findAllByOwner(Profile owner);
    List<PersistLoanAccount> findAllByNextPaymentDueDate(LocalDate date);
}
