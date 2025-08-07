package net.mestizoftware.application.service;

import lombok.RequiredArgsConstructor;
import net.mestizoftware.domain.model.User;
import net.mestizoftware.domain.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public void update(User user) {
        userRepository.save(user);
    }

}
