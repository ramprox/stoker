package ru.stoker.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.stoker.database.entity.User;
import ru.stoker.database.repository.UserRepository;

@Service
public class StokerUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public StokerUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findByCredentialsLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException(login));
        return new StokerUserDetails(user);
    }
}
