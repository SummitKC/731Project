package org.cps731.project.team.cps731.pomodoro.controllers.auth;

import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletResponse;
import org.cps731.project.team.cps731.pomodoro.dto.LoginRequestBody;
import org.cps731.project.team.cps731.pomodoro.security.auth.JwtUtil;
import org.cps731.project.team.cps731.pomodoro.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class LoginController {

    private final AuthService authService;

    @Autowired
    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/student/login")
    public ResponseEntity<String> studentLogin(@RequestBody LoginRequestBody body) {
        try {
            return ResponseEntity.ok(authService.studentLogin(body));
        } catch (AuthException e) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED)
                    .build();
        }
    }

}