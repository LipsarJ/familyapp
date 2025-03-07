package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.controlleradvice.Errors;
import org.example.controlleradvice.SimpleResponse;
import org.example.dto.request.LoginDTO;
import org.example.dto.request.SignUpDTO;
import org.example.dto.response.AuthResponseDTO;
import org.example.service.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("newAPI/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;

    @PostMapping("register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpDTO signUpDTO) {
        authService.registerUser(signUpDTO);
        return ResponseEntity.ok(new SimpleResponse("User registered successfully!", null));
    }

    @PostMapping("signin")
    public ResponseEntity<?> signInUser(@Valid @RequestBody LoginDTO loginDto) {
        try {
            AuthResponseDTO response = authService.signIn(loginDto);

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, response.getAccessToken())
                    .header(HttpHeaders.SET_COOKIE, response.getRefreshToken())
                    .body(response.getUserDTO());

        } catch (AuthenticationException ex) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED)
                    .body(new SimpleResponse("Invalid username or password", Errors.BAD_CREDENTIALS));
        }
    }

    @PostMapping("signout")
    public ResponseEntity<?> signOutUser() {

        AuthResponseDTO response = authService.signOut();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, response.getAccessToken())
                .header(HttpHeaders.SET_COOKIE, response.getRefreshToken())
                .body(new SimpleResponse("You've been signed out!", null));
    }

    @PostMapping("refreshtoken")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {

        AuthResponseDTO authResponseDTO = authService.refreshToken(request);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, authResponseDTO.getAccessToken())
                .body(new SimpleResponse("Token is refreshed successfully!", null));
    }
}
