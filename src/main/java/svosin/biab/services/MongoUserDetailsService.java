package svosin.biab.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import svosin.biab.entities.Profile;
import svosin.biab.repos.ProfileRepository;

import java.util.Collections;
import java.util.List;

@Component
public class MongoUserDetailsService implements UserDetailsService {
    @Autowired
    private ProfileRepository profileRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Profile profile = profileRepository.findByUsername(username);
        if (profile == null) {
            throw new UsernameNotFoundException("User not found");
        }
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("user"));
        return new User(profile.getUsername(), profile.getPasshash(), authorities);
    }
}