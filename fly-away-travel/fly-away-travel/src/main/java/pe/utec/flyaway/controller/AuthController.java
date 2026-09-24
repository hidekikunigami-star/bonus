package pe.utec.flyaway.controller;

import pe.utec.flyaway.dto.*;
import pe.utec.flyaway.security.JwtService;
import pe.utec.flyaway.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService users;
    private final JwtService jwt;

    public AuthController(UserService users, JwtService jwt) {
        this.users = users;
        this.jwt = jwt;
    }

    @PostMapping("/login")
    TokenResponse login(@Valid @RequestBody LoginRequest request) {
        var user = users.authenticate(request.email(), request.password());
        return new TokenResponse(jwt.generate(user.getId(), user.getEmail()));
    }
}
