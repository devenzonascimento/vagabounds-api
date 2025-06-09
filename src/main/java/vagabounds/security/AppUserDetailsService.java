package vagabounds.security;

import vagabounds.models.User;
import vagabounds.repositories.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;

import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

@Service
public class AppUserDetailsService implements UserDetailsService {
    private final UserRepository repo;
    public AppUserDetailsService(UserRepository repo) { this.repo = repo; }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        var authorities = user.getRoles().stream()
                .map(r -> new SimpleGrantedAuthority(r.name()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}
