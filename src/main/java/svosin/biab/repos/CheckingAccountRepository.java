package svosin.biab.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import svosin.biab.entities.CheckingAccount;
import svosin.biab.entities.Profile;

import java.util.List;

public interface CheckingAccountRepository extends MongoRepository<CheckingAccount, String> {
    List<CheckingAccount> findAllByOwner(Profile owner);
}
