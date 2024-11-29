package org.cps731.project.team.cps731.pomodoro.services;

import jakarta.security.auth.message.AuthException;
import org.cps731.project.team.cps731.pomodoro.data.model.user.Professor;
import org.cps731.project.team.cps731.pomodoro.data.model.user.Student;
import org.cps731.project.team.cps731.pomodoro.data.model.user.User;
import org.cps731.project.team.cps731.pomodoro.data.model.user.UserType;
import org.cps731.project.team.cps731.pomodoro.data.repo.user.ProfessorRepo;
import org.cps731.project.team.cps731.pomodoro.data.repo.user.StudentRepo;
import org.cps731.project.team.cps731.pomodoro.data.repo.user.UserRepo;
import org.cps731.project.team.cps731.pomodoro.dto.auth.LoginRequestDTO;
import org.cps731.project.team.cps731.pomodoro.dto.auth.RegisterProfessorRequestDTO;
import org.cps731.project.team.cps731.pomodoro.dto.auth.RegisterStudentRequestDTO;
import org.cps731.project.team.cps731.pomodoro.security.auth.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final JwtUtil jwtUtil;
    private final StudentService studentService;
    private final ProfessorService professorService;
    private final PasswordEncoder passwordEncoder;
    private final StudentRepo studentRepo;
    private final ProfessorRepo professorRepo;
    private final UserRepo userRepo;

    @Autowired
    public AuthService(JwtUtil jwtUtil,
                       StudentService studentService,
                       ProfessorService professorService,
                       PasswordEncoder passwordEncoder,
                       StudentRepo studentRepo,
                       ProfessorRepo professorRepo,
                       UserRepo userRepo) {
        this.jwtUtil = jwtUtil;
        this.studentService = studentService;
        this.professorService = professorService;
        this.passwordEncoder = passwordEncoder;
        this.studentRepo = studentRepo;
        this.professorRepo = professorRepo;
        this.userRepo = userRepo;
    }

    public boolean studentRegister(RegisterStudentRequestDTO registerRequest) {
        if (userRepo.existsByEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException("Account using this email already exists");
        } else if (studentRepo.existsByStudentID(registerRequest.getStudentID())) {
            throw new IllegalArgumentException("Student with this ID already exists");
        }
        var user = new User(registerRequest.getName(), registerRequest.getEmail(), passwordEncoder.encode(registerRequest.getPassword()), UserType.STUDENT);
        userRepo.save(user);
        var student = studentService.createStudent(new Student(user, registerRequest.getStudentID()));
        return student != null;
    }

    public String studentLogin(LoginRequestDTO loginRequest) throws AuthException {
        var student = studentService.getStudentByEmail(loginRequest.getEmail());
        if (student == null) {
            throw new AuthException("Student not found");
        } else if (!passwordEncoder.matches(loginRequest.getPassword(), student.getUser().getPassword())) {
            throw new AuthException("Wrong password");
        }
        return jwtUtil.generateToken(student.getUser().getId().toString(), student.getUser().getName());
    }

    public boolean professorRegister(RegisterProfessorRequestDTO registerRequest) {
        if (userRepo.existsByEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException("Account using this email already exists");
        } else if (professorRepo.existsByEmployeeID(registerRequest.getProfessorID())) {
            throw new IllegalArgumentException("Professor with this ID already exists");
        }
        var user = new User(registerRequest.getName(),registerRequest.getEmail(), passwordEncoder.encode(registerRequest.getPassword()), UserType.PROFESSOR);
        userRepo.save(user);
        var professor = professorService.createProfessor(new Professor(user, registerRequest.getProfessorID()));
        return professor != null;
    }

    public String professorLogin(LoginRequestDTO loginRequest) throws AuthException {
        var professor = professorService.getProfessorByEmail(loginRequest.getEmail());
        if (professor == null) {
            throw new AuthException("Professor not found");
        } else if (!passwordEncoder.matches(loginRequest.getPassword(), professor.getUser().getPassword())) {
            throw new AuthException("Wrong password");
        }
        return jwtUtil.generateToken(professor.getUser().getId().toString(), professor.getUser().getName());
    }

}
