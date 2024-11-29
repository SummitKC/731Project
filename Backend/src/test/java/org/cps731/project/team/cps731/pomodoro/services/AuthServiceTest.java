package org.cps731.project.team.cps731.pomodoro.services;

import org.cps731.project.team.cps731.pomodoro.data.repo.user.UserRepo;
import org.cps731.project.team.cps731.pomodoro.security.auth.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes={
        AuthService.class,
        JwtUtil.class,
        StudentService.class,
        ProfessorService.class,
        PasswordEncoder.class,
        UserRepo.class,
})
@ActiveProfiles("test")
public class AuthServiceTest {

    @MockBean
    private JwtUtil jwtUtil;
    @MockBean
    private StudentService studentService;
    @MockBean
    private ProfessorService professorService;
    @MockBean
    private UserRepo userRepo;
    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    public void contextLoads() {

    }

}
