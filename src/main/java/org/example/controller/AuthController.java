package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.controlleradvice.Errors;
import org.example.controlleradvice.SimpleResponse;
import org.example.dto.request.LoginDTO;
import org.example.dto.request.SignUpDTO;
import org.example.dto.response.UserLoginDTO;
import org.example.entity.RefreshToken;
import org.example.entity.User;
import org.example.exception.TokenRefreshException;
import org.example.repo.RefreshTokenRepo;
import org.example.repo.UserRepo;
import org.example.sequrity.JwtUtils;
import org.example.sequrity.UserDetailsImpl;
import org.example.sequrity.service.RefreshTokenService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final PasswordEncoder encoder;
    private final UserRepo userRepo;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepo refreshTokenRepo;

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

    @PostMapping("signin")
    public ResponseEntity<?> signInUser(@Valid @RequestBody LoginDTO loginDto) {

        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginDto.username(), loginDto.password()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

            ResponseCookie jwtRefreshCookie = jwtUtils.generateRefreshJwtCookie(refreshToken.getToken());

            UserLoginDTO userLoginDto = new UserLoginDTO(userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail());

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                    .body(userLoginDto);
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED)
                    .body(new SimpleResponse("Invalid username or password", Errors.BAD_CREDENTIALS));
        }
    }

    @PostMapping("signout")
    public ResponseEntity<?> signOutUser() {

        ResponseCookie jwtCookie = jwtUtils.getCleanJwtCookie();
        ResponseCookie jwtRefreshCookie = jwtUtils.getCleanJwtRefreshCookie();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .body(new SimpleResponse("You've been signed out!", null));
    }

    @PostMapping("refreshtoken")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);

        if (refreshToken != null && !refreshToken.isEmpty()) {
            return refreshTokenRepo.findByToken(refreshToken)
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshToken::getUser)
                    .map(user -> {
                        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(user);

                        return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                                .body(new SimpleResponse("Token is refreshed successfully!", null));
                    })
                    .orElseThrow(() -> new TokenRefreshException(refreshToken, "Refresh token is not in the database!"));
        }

        return ResponseEntity.badRequest().body(new SimpleResponse("Refresh Token is empty!", null));
    }
}
