package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.controlleradvice.Errors;
import org.example.controlleradvice.SimpleResponse;
import org.example.dto.request.SignUpDTO;
import org.example.entity.User;
import org.example.repo.UserRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final PasswordEncoder encoder;
    private final UserRepo userRepo;

    @PostMapping("register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpDTO signUpDTO) {

        if (userRepo.existsUserByUsername(signUpDTO.username())) {
            return ResponseEntity.badRequest().body(new SimpleResponse("Error: Username is taken", Errors.USERNAME_TAKEN));
        }
        if (userRepo.existsUserByEmail(signUpDTO.email())) {
            return ResponseEntity.badRequest().body(new SimpleResponse("Error: Email is taken", Errors.EMAIL_TAKEN));
        }

        User user = new User();
        user.setUsername(signUpDTO.username());
        user.setEmail(signUpDTO.email());
        user.setPassword(encoder.encode(signUpDTO.password()));
        user.setFirstname(signUpDTO.firstname());
        user.setMiddlename(signUpDTO.middlename());
        user.setLastname(signUpDTO.lastname());

        userRepo.save(user);

        return ResponseEntity.ok(new SimpleResponse("User registered successfully!", null));
    }
}
