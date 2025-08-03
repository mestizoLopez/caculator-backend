package net.mestizoftware.web.controller;

import lombok.RequiredArgsConstructor;
import net.mestizoftware.application.service.AuthService;
import net.mestizoftware.domain.model.User;
import net.mestizoftware.web.dto.AuthRequestDto;
import net.mestizoftware.web.dto.AuthResponseDto;
import net.mestizoftware.web.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody AuthRequestDto request) {
        User user = authService.register(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(new UserDto(
                user.getId(), user.getUsername(), user.getStatus(), user.getBalance()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto request) {
        return authService.login(request.getUsername(), request.getPassword())
                .map(token -> ResponseEntity.ok(new AuthResponseDto(token)))
                .orElseGet(() -> ResponseEntity.status(401).body( new AuthResponseDto("Invalid credentials")));
    }

}
