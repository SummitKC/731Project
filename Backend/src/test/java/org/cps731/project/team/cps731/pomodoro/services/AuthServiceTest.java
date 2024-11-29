package org.cps731.project.team.cps731.pomodoro.services;

import org.cps731.project.team.cps731.pomodoro.data.model.user.Student;
import org.cps731.project.team.cps731.pomodoro.data.repo.user.StudentRepo;
import org.cps731.project.team.cps731.pomodoro.data.repo.user.UserRepo;
import org.cps731.project.team.cps731.pomodoro.dto.auth.RegisterStudentRequestDTO;
import org.cps731.project.team.cps731.pomodoro.security.auth.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = {AuthService.class,
        JwtUtil.class,
        StudentService.class,
        ProfessorService.class,
        UserRepo.class,
        StudentRepo.class,
        PasswordEncoder.class})
@ActiveProfiles("test")
public class AuthServiceTest {

    @Autowired
    private AuthService authService;
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
    @MockBean
    private StudentRepo studentRepo;

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenAccountWithEmailAlreadyExists() {
        var registerRequest = registerRequest();
        when(userRepo.existsByEmail(registerRequest.getEmail()))
                .thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authService.studentRegister(registerRequest));
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenAccountWithUserIDAlreadyExists() {
        var registerRequest = registerRequest();
        when(studentRepo.existsByStudentID(registerRequest.getStudentID()))
                .thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authService.studentRegister(registerRequest));
    }

    @Test
    public void shouldReturnTrueWhenStudentRegisterRequestIsValid() {
        var registerRequest = registerRequest();
        when(studentService.createStudent(any(Student.class)))
                .thenReturn(mock(Student.class));

        var result = authService.studentRegister(registerRequest);

        assertThat(result, equalTo(true));
    }

    private static RegisterStudentRequestDTO registerRequest() {
        return new RegisterStudentRequestDTO(1L, "John", "john.amiscaray@torontomu.ca", "password");
    }

}
