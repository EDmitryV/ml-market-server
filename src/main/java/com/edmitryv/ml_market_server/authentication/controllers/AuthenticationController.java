package com.edmitryv.ml_market_server.authentication.controllers;

import com.edmitryv.ml_market_server.authentication.mail.RegistrationCompleteEvent;
import com.edmitryv.ml_market_server.authentication.models.AuthenticationRequest;
import com.edmitryv.ml_market_server.authentication.models.AuthenticationResponse;
import com.edmitryv.ml_market_server.authentication.models.RegisterRequest;
import com.edmitryv.ml_market_server.authentication.models.VerificationToken;
import com.edmitryv.ml_market_server.authentication.repos.VerificationTokenRepository;
import com.edmitryv.ml_market_server.authentication.services.AuthenticationService;
import com.edmitryv.ml_market_server.core.models.User;
import com.edmitryv.ml_market_server.core.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/authentication")
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired
    private final AuthenticationService authService;
    @Autowired
    private final UserService userService;
    @Autowired
    private final Environment env;
    @Autowired
    private final VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private final AuthenticationService authenticationService;
    @Autowired
    private final ApplicationEventPublisher publisher;

    private String getUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    @PostMapping("/registration")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request,
            final HttpServletRequest httpServletRequest
    ) {
        try {
            User user = authService.register(request);
            publisher.publishEvent(
                    new RegistrationCompleteEvent(
                            user,
                            "http://" + httpServletRequest.getServerName() + ":"
                                    + httpServletRequest.getServerPort() + httpServletRequest.getContextPath()));
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/authentication")
    public ResponseEntity<?> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        try {
            return ResponseEntity.ok(authService.authenticate(request));
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String sStackTrace = sw.toString();
            return ResponseEntity.badRequest().body(e.getMessage() + sStackTrace);
        }

    }

    @GetMapping("/registration/check-email")
    public ResponseEntity<?> checkEmail(String email) {
        if (userService.findByEmail(email) != null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok().build();
        }
    }

    @GetMapping("/registration/verify-email")
    public String verifyEmail(@RequestParam("token") String token) {
        VerificationToken theToken = verificationTokenRepository.findByToken(token);
        if (theToken.getUser().isEnabled()) {
            return "Этот адрес электронной почты уже был подтвержден, пожалуйста войдите в аккаунт.";
        }
        String verificationResult = authenticationService.validateToken(token);
        if (verificationResult.equalsIgnoreCase("valid")) {
            return "Адрес электронной почты подтвержден. Теперь вы можете войти в аккаунт в приложении.";
        }
        return "Неправильный верификационный токен!";
    }

    @GetMapping("/registration/check-username")
    public ResponseEntity<?> checkUsername(String username) {
        if (userService.findByUsername(username) != null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok().build();
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
       return  ResponseEntity.ok(authService.refreshToken(request, response));
    }

    @GetMapping("/exists/by-username/{username}")
    public ResponseEntity<Boolean> existsByUsername(@PathVariable("username") String username) {
        return new ResponseEntity<>(userService.existsByUsername(username), HttpStatus.OK);
    }

    @GetMapping("/exists/by-email/{email}")
    public ResponseEntity<Boolean> existsByEmail(@PathVariable("email") String email) {
        return new ResponseEntity<>(userService.existsByEmail(email), HttpStatus.OK);
    }

    @GetMapping("/get-tokens-expiration-time")
    public ResponseEntity<Map<String, String>> getTokensExpirationTime() {
        Map<String, String> response = new HashMap<>();
        response.put("auth_expiration", env.getProperty("application.security.jwt.expiration"));
        response.put("refresh_expiration", env.getProperty("application.security.jwt.refresh-token.expiration"));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
