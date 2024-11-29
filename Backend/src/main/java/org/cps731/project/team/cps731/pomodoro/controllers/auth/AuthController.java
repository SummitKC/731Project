package org.cps731.project.team.cps731.pomodoro.controllers.auth;

import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletResponse;
import org.cps731.project.team.cps731.pomodoro.dto.auth.LoginRequestDTO;
import org.cps731.project.team.cps731.pomodoro.dto.auth.RegisterProfessorRequestDTO;
import org.cps731.project.team.cps731.pomodoro.dto.auth.RegisterStudentRequestDTO;
import org.cps731.project.team.cps731.pomodoro.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/student/login")
    public ResponseEntity<String> studentLogin(@RequestBody LoginRequestDTO body) {
        try {
            return ResponseEntity.ok(authService.studentLogin(body));
        } catch (AuthException e) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED)
                    .build();
        }
    }

    @PostMapping("/student/register")
    public ResponseEntity<Void> studentRegister(@RequestBody RegisterStudentRequestDTO body) {
        if (authService.studentRegister(body)) {
            return ResponseEntity.noContent().build();
        } else {
            throw new RuntimeException("Unable to register student");
        }
    }

    @PostMapping("/professor/login")
    public ResponseEntity<String> professorLogin(@RequestBody LoginRequestDTO body) {
        try {
            return ResponseEntity.ok(authService.professorLogin(body));
        } catch (AuthException e) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED)
                    .build();
        }
    }

    @PostMapping("/professor/register")
    public ResponseEntity<Void> professorRegister(@RequestBody RegisterProfessorRequestDTO body) {
        if (authService.professorRegister(body)) {
            return ResponseEntity.noContent().build();
        } else {
            throw new RuntimeException("Unable to register professor");
        }
    }

}
