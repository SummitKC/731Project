package org.cps731.project.team.cps731.pomodoro.controllers.auth;

import jakarta.security.auth.message.AuthException;
import org.cps731.project.team.cps731.pomodoro.dto.auth.LoginRequestDTO;
import org.cps731.project.team.cps731.pomodoro.dto.auth.RegisterRequestDTO;
import org.cps731.project.team.cps731.pomodoro.services.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AuthControllerTest {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @MockBean
    private AuthService authService;

    @Test
    public void shouldYieldHttp204WhenRegisteringStudent() {
        when(authService.studentRegister(registerRequest())).thenReturn(true);

        var requestEntity = new HttpEntity<>(registerRequest(), null);
        var response = restTemplate
                .exchange(
                        "http://localhost:" + port + "/api/auth/student/register",
                        HttpMethod.POST,
                        requestEntity,
                        Void.class
                );

        assertThat(response.getStatusCode(), equalTo(HttpStatusCode.valueOf(204)));
    }

    @Test
    public void shouldYieldHttp204WhenRegisteringProfessor() {
        when(authService.professorRegister(registerRequest())).thenReturn(true);

        var requestEntity = new HttpEntity<>(registerRequest(), null);
        var response = restTemplate
                .exchange(
                        "http://localhost:" + port + "/api/auth/professor/register",
                        HttpMethod.POST,
                        requestEntity,
                        Void.class
                );

        assertThat(response.getStatusCode(), equalTo(HttpStatusCode.valueOf(204)));
    }

    @Test
    public void shouldYieldJWTWhenLoggingInStudent() throws AuthException {
        when(authService.studentLogin(loginRequest())).thenReturn(jwtString());

        var requestEntity = new HttpEntity<>(loginRequest(), null);
        var response = restTemplate
                .exchange(
                        "http://localhost:" + port + "/api/auth/student/login",
                        HttpMethod.POST,
                        requestEntity,
                        String.class
                );

        assertThat(response.getStatusCode(), equalTo(HttpStatusCode.valueOf(200)));
        assertThat(response.getBody(), equalTo(jwtString()));
    }

    @Test
    public void shouldYieldJWTWhenLoggingInProfessor() throws AuthException {
        when(authService.professorLogin(loginRequest())).thenReturn(jwtString());

        var requestEntity = new HttpEntity<>(loginRequest(), null);
        var response = restTemplate
                .exchange(
                        "http://localhost:" + port + "/api/auth/professor/login",
                        HttpMethod.POST,
                        requestEntity,
                        String.class
                );

        assertThat(response.getStatusCode(), equalTo(HttpStatusCode.valueOf(200)));
        assertThat(response.getBody(), equalTo(jwtString()));
    }

    private RegisterRequestDTO registerRequest() {
        return new RegisterRequestDTO(1L, "John", "john.smith@torontomu.ca", "password");
    }

    private LoginRequestDTO loginRequest() {
        return new LoginRequestDTO("john.smith@torontomu.ca", "password");
    }

    private String jwtString() {
        return "helloworld";
    }

}
