package org.cps731.project.team.cps731.pomodoro.services;

import jakarta.security.auth.message.AuthException;
import org.cps731.project.team.cps731.pomodoro.data.model.user.Professor;
import org.cps731.project.team.cps731.pomodoro.data.model.user.Student;
import org.cps731.project.team.cps731.pomodoro.data.model.user.User;
import org.cps731.project.team.cps731.pomodoro.data.model.user.UserType;
import org.cps731.project.team.cps731.pomodoro.data.repo.user.UserRepo;
import org.cps731.project.team.cps731.pomodoro.dto.auth.AuthRequestDTO;
import org.cps731.project.team.cps731.pomodoro.security.auth.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final JwtUtil jwtUtil;
    private final StudentService studentService;
    private final ProfessorService professorService;
    private final PasswordEncoder encoder;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(JwtUtil jwtUtil,
                       StudentService studentService,
                       ProfessorService professorService,
                       PasswordEncoder encoder,
                       UserRepo userRepo,
                       PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.studentService = studentService;
        this.professorService = professorService;
        this.encoder = encoder;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean studentRegister(AuthRequestDTO registerRequest) {
        if (userRepo.existsByEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException("Account using this email already exists");
        }
        var user = new User(registerRequest.getEmail(), passwordEncoder.encode(registerRequest.getPassword()), UserType.STUDENT);
        userRepo.save(user);
        var student = studentService.createStudent(new Student(user));
        return student != null;
    }

    public String studentLogin(AuthRequestDTO loginRequest) throws AuthException {
        var student = studentService.getStudentByEmail(loginRequest.getEmail());
        if (student == null) {
            throw new AuthException("Student not found");
        } else if (!encoder.matches(loginRequest.getPassword(), student.getUser().getPassword())) {
            throw new AuthException("Wrong password");
        }
        return jwtUtil.generateToken(student.getUser().getId().toString());
    }

    public boolean professorRegister(AuthRequestDTO registerRequest) {
        if (userRepo.existsByEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException("Account using this email already exists");
        }
        var user = new User(registerRequest.getEmail(), passwordEncoder.encode(registerRequest.getPassword()), UserType.PROFESSOR);
        userRepo.save(user);
        var professor = professorService.createProfessor(new Professor(user));
        return professor != null;
    }

    public String professorLogin(AuthRequestDTO loginRequest) throws AuthException {
        var professor = professorService.getProfessorByEmail(loginRequest.getEmail());
        if (professor == null) {
            throw new AuthException("Professor not found");
        } else if (!encoder.matches(loginRequest.getPassword(), professor.getUser().getPassword())) {
            throw new AuthException("Wrong password");
        }
        return jwtUtil.generateToken(professor.getUser().getId().toString());
    }

}
