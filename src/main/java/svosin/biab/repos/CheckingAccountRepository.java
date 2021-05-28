package svosin.biab.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import svosin.biab.entities.CheckingAccount;
import svosin.biab.entities.Profile;
import svosin.biab.persistEntities.PersistCheckingAccount;

import java.util.List;

public interface CheckingAccountRepository extends MongoRepository<PersistCheckingAccount, String> {
    List<PersistCheckingAccount> findAllByOwner(Profile owner);
}
