package org.cps731.project.team.cps731.pomodoro.services;

import jakarta.security.auth.message.AuthException;
import org.cps731.project.team.cps731.pomodoro.dto.LoginRequestBody;
import org.cps731.project.team.cps731.pomodoro.security.auth.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final JwtUtil jwtUtil;
    private final StudentService studentService;
    private final PasswordEncoder encoder;

    @Autowired
    public AuthService(JwtUtil jwtUtil, StudentService studentService, PasswordEncoder encoder) {
        this.jwtUtil = jwtUtil;
        this.studentService = studentService;
        this.encoder = encoder;
    }

    public String studentLogin(LoginRequestBody loginRequest) throws AuthException {
        var student = studentService.getStudentByEmail(loginRequest.getEmail());
        if (student == null) {
            throw new AuthException("Student not found");
        } else if (!encoder.matches(loginRequest.getPassword(), student.getUser().getPassword())) {
            throw new AuthException("Wrong password");
        }
        return jwtUtil.generateToken(student.getUser().getId().toString());
    }

}
