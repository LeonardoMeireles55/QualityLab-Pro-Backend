package leonardo.labutilities.qualitylabpro.services;

import leonardo.labutilities.qualitylabpro.repository.UserRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthService implements UserDetailsService {

    private final UserRepositoryCustom userRepositoryCustom;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepositoryCustom.findByUsername(username);
        if (user == null) {
            log.error("User not found.");
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return user;
    }
}
