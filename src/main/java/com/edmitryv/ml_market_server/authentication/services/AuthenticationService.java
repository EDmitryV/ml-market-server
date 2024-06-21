package com.edmitryv.ml_market_server.authentication.services;

import com.edmitryv.ml_market_server.authentication.exceptions.UserNotVerifiedException;
import com.edmitryv.ml_market_server.authentication.exceptions.UserAlreadyExistsException;
import com.edmitryv.ml_market_server.authentication.exceptions.UserNotFoundException;
import com.edmitryv.ml_market_server.authentication.models.*;
import com.edmitryv.ml_market_server.authentication.repos.TokenRepository;
import com.edmitryv.ml_market_server.authentication.repos.VerificationTokenRepository;
import com.edmitryv.ml_market_server.core.models.CustomerAccount;
import com.edmitryv.ml_market_server.core.models.DeveloperAccount;
import com.edmitryv.ml_market_server.core.models.User;
import com.edmitryv.ml_market_server.core.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Calendar;
import java.io.IOException;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    @Autowired
    private final UserService userService;
    @Autowired
    private final TokenRepository tokenRepository;
    @Autowired
    private final VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public User register(RegisterRequest request) {
        User savedUser = userService.findByEmail(request.getEmail());
        if (savedUser != null) {
            if(savedUser.isEnabled()) {
                throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists");
            }else{
                userService.deleteUser(savedUser);
            }
        }
        if(userService.findByUsername(request.getUsername())!=null){
            throw new UserAlreadyExistsException("User with username " + request.getUsername() + " already exists");
        }
        User user = new User(
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                Role.USER
//                request.getAuthority() == null ? Authority.USER : request.getAuthority()
        );
        return userService.save(user);
    }
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user = userService.findByUsername(request.getUsername());
        if (user == null) {
            throw new UserNotFoundException("User with username: " + request.getUsername() + " not found.");
        }
        if (!user.isEnabled()) {
            throw new UserNotVerifiedException("User:: " + request.getUsername() + " not verified.");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }
    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }
    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
    public AuthenticationResponse refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Problems with bearer");
        }
        refreshToken = authHeader.substring(7);
        username = jwtService.extractUsername(refreshToken);
        if (username != null) {
            var user = userService.findByUsername(username);
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
                return authResponse;

            }
        }
        throw new RuntimeException("User not exists");
    }

    public void saveUserVerificationToken(User theUser, String token) {
        var verificationToken = new VerificationToken(token, theUser);
        verificationTokenRepository.save(verificationToken);
    }
    public String validateToken(String theToken) {
        VerificationToken token = verificationTokenRepository.findByToken(theToken);
        if(token == null){
            return "Invalid verification token";
        }
        User user = token.getUser();
        Calendar calendar = Calendar.getInstance();
        if ((token.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0){
            verificationTokenRepository.delete(token);
            return "Token already expired";
        }
        user.setEnabled(true);
        user.setDeveloperAccount(new DeveloperAccount());
        user.setCustomerAccount(new CustomerAccount());
        userService.save(user);
        return "valid";
    }
}
