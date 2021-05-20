package svosin.biab.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import svosin.biab.entities.CheckingAccount;
import svosin.biab.entities.LoanAccount;
import svosin.biab.entities.Profile;

public interface LoanAccountRepository extends MongoRepository<LoanAccount, String> {
    Iterable<LoanAccount> findAllByOwner(Profile owner);
}
