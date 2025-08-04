package net.mestizoftware.application.service;

import lombok.RequiredArgsConstructor;
import net.mestizoftware.domain.model.User;
import net.mestizoftware.domain.repository.UserRepository;
import net.mestizoftware.infrastructure.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public Optional<String> login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .map(jwtUtil::generateToken);
    }

    public User register(String username, String rawPassword) {
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(rawPassword))
                .status("active")
                .balance(100.0) // Default starting balance
                .build();
        return userRepository.save(user);
    }

}
