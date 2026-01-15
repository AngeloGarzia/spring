package fr.diginamic.recencement.Securite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

//@Service
public class MemoireService implements UserDetailsService {
    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (username.equals("gdupont")) {
            return new Utilisateur(username, encoder.encode("user1234"),
                    List.of(new Role("ROLE_USER")));
        }
        if (username.equals("admin")) {
            return new Utilisateur(username, encoder.encode("admin1234"),
                    List.of(new Role("ROLE_ADMIN")));
        }
        throw new UsernameNotFoundException("Utilisateur inconnu: " + username);
    }
}
