package ru.artemka.demo.authorization.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.artemka.demo.model.User;
import ru.artemka.demo.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmailIgnoreCase(login);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }
}
