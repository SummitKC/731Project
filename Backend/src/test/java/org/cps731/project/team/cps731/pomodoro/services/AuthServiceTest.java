package org.cps731.project.team.cps731.pomodoro.services;

import org.cps731.project.team.cps731.pomodoro.data.model.user.Professor;
import org.cps731.project.team.cps731.pomodoro.data.model.user.Student;
import org.cps731.project.team.cps731.pomodoro.data.repo.user.ProfessorRepo;
import org.cps731.project.team.cps731.pomodoro.data.repo.user.StudentRepo;
import org.cps731.project.team.cps731.pomodoro.data.repo.user.UserRepo;
import org.cps731.project.team.cps731.pomodoro.dto.auth.RegisterProfessorRequestDTO;
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
        ProfessorRepo.class,
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
    @MockBean
    private ProfessorRepo professorRepo;

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenAccountWithEmailAlreadyExists() {
        var registerRequest = registerStudentRequest();
        when(userRepo.existsByEmail(registerRequest.getEmail()))
                .thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authService.studentRegister(registerRequest));
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenAccountWithStudentIDAlreadyExists() {
        var registerRequest = registerStudentRequest();
        when(studentRepo.existsByStudentID(registerRequest.getStudentID()))
                .thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authService.studentRegister(registerRequest));
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenAccountWithProfessorIDAlreadyExists() {
        var registerRequest = registerProfessorRequest();
        when(professorRepo.existsByEmployeeID(registerRequest.getProfessorID()))
                .thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authService.professorRegister(registerRequest));
    }

    @Test
    public void shouldReturnTrueWhenStudentRegisterStudentRequestIsValid() {
        var registerRequest = registerStudentRequest();
        when(studentService.createStudent(any(Student.class)))
                .thenReturn(mock(Student.class));

        var result = authService.studentRegister(registerRequest);

        assertThat(result, equalTo(true));
    }

    @Test
    public void shouldReturnTrueWhenProfessorRegisterProfessorRequestIsValid() {
        var registerRequest = registerProfessorRequest();
        when(professorService.createProfessor(any(Professor.class)))
                .thenReturn(mock(Professor.class));

        var result = authService.professorRegister(registerRequest);

        assertThat(result, equalTo(true));
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionForStudentRegisterWhenEmailAlreadyExists() {
        var registerRequest = registerStudentRequest();
        when(userRepo.existsByEmail(registerRequest.getEmail()))
                .thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authService.studentRegister(registerRequest));
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionForProfessorRegisterWhenEmailAlreadyExists() {
        var registerRequest = registerProfessorRequest();
        when(userRepo.existsByEmail(registerRequest.getEmail()))
                .thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authService.professorRegister(registerRequest));
    }

    private static RegisterStudentRequestDTO registerStudentRequest() {
        return new RegisterStudentRequestDTO(1L, "John", "john.amiscaray@torontomu.ca", "password");
    }

    private static RegisterProfessorRequestDTO registerProfessorRequest() {
        return new RegisterProfessorRequestDTO(1L, "John", "john.amiscaray@torontomu.ca", "password");
    }

}
