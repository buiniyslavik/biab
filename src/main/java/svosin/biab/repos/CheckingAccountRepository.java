package svosin.biab.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import svosin.biab.entities.CheckingAccount;
import svosin.biab.entities.Profile;

public interface CheckingAccountRepository extends MongoRepository<CheckingAccount, String> {
    Iterable<CheckingAccount> findAllByOwner(Profile owner);
}
