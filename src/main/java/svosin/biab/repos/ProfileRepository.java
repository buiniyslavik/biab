package svosin.biab.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import svosin.biab.entities.Profile;

public interface ProfileRepository extends MongoRepository<Profile, String> {
    Profile findByUsername(String username);
}
