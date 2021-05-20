package svosin.biab.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import svosin.biab.entities.LoanRequest;

public interface LoanRequestRepository extends MongoRepository<LoanRequest, String> {
}
