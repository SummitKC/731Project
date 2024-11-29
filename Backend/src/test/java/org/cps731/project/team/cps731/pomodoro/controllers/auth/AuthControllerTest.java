package org.cps731.project.team.cps731.pomodoro.controllers.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.cps731.project.team.cps731.pomodoro.config.WebTestConfig;
import org.cps731.project.team.cps731.pomodoro.controllers.student.StudentAnalyticsController;
import org.cps731.project.team.cps731.pomodoro.dto.auth.LoginRequestDTO;
import org.cps731.project.team.cps731.pomodoro.dto.auth.RegisterProfessorRequestDTO;
import org.cps731.project.team.cps731.pomodoro.dto.auth.RegisterStudentRequestDTO;
import org.cps731.project.team.cps731.pomodoro.security.auth.JwtUtil;
import org.cps731.project.team.cps731.pomodoro.security.filter.JwtAuthenticationFilter;
import org.cps731.project.team.cps731.pomodoro.services.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class)
@Import(WebTestConfig.class)
@ActiveProfiles("test")
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthService authService;
    @MockBean
    private JwtUtil jwtUtil;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final ObjectWriter WRITER = MAPPER.writer().withDefaultPrettyPrinter();

    @BeforeEach
    public void setupTest() throws ServletException, IOException {
        doAnswer(invocation -> {
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken("John Smith", null, Set.of())
            );
            FilterChain filterChain = invocation.getArgument(2, FilterChain.class);
            filterChain.doFilter(invocation.getArgument(0), invocation.getArgument(1));
            return null;
        }).when(jwtAuthenticationFilter).doFilter(any(), any(), any());
    }

    @Test
    public void shouldYieldHttp204WhenRegisteringStudent() throws Exception {
        when(authService.studentRegister(registerStudentRequest())).thenReturn(true);

        mockMvc.perform(post("/auth/student/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(WRITER.writeValueAsString(registerStudentRequest())))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldYieldHttp204WhenRegisteringProfessor() throws Exception {
        when(authService.professorRegister(registerProfessorRequest())).thenReturn(true);

        mockMvc.perform(post("/auth/professor/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(WRITER.writeValueAsString(registerProfessorRequest())))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldYieldJWTWhenLoggingInStudent() throws Exception {
        when(authService.studentLogin(loginRequest())).thenReturn(jwtString());

        mockMvc.perform(post("/auth/student/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(WRITER.writeValueAsString(registerStudentRequest())))
                        .andExpect(result -> assertThat(result.getResponse().getContentAsString(), equalTo(jwtString())));
    }

    @Test
    public void shouldYieldJWTWhenLoggingInProfessor() throws Exception {
        when(authService.professorLogin(loginRequest())).thenReturn(jwtString());

        mockMvc.perform(post("/auth/professor/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(WRITER.writeValueAsString(loginRequest())))
                .andExpect(result -> assertThat(result.getResponse().getContentAsString(), equalTo(jwtString())));
    }

    private RegisterStudentRequestDTO registerStudentRequest() {
        return new RegisterStudentRequestDTO(1L, "John", "john.smith@torontomu.ca", "password");
    }

    private RegisterProfessorRequestDTO registerProfessorRequest() {
        return new RegisterProfessorRequestDTO(1L, "John", "john.smith@torontomu.ca", "password");
    }

    private LoginRequestDTO loginRequest() {
        return new LoginRequestDTO("john.smith@torontomu.ca", "password");
    }

    private String jwtString() {
        return "helloworld";
    }

}
