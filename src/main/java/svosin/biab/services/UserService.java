package svosin.biab.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import svosin.biab.entities.Profile;
import svosin.biab.repos.ProfileRepository;

@Service
public class UserService {
    @Autowired
    ProfileRepository profileRepository;
    public Profile createProfile(String username, String plainpass) {
        return profileRepository.save(
                new Profile(username, plainpass)
        );
    }
}
