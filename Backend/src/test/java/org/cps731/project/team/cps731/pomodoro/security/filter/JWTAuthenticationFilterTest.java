package org.cps731.project.team.cps731.pomodoro.security.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.cps731.project.team.cps731.pomodoro.data.model.user.User;
import org.cps731.project.team.cps731.pomodoro.data.model.user.UserType;
import org.cps731.project.team.cps731.pomodoro.security.auth.JwtUtil;
import org.cps731.project.team.cps731.pomodoro.security.principal.AppUserDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;

import static org.mockito.Mockito.*;

@SpringBootTest(classes = {JwtAuthenticationFilter.class})
public class JWTAuthenticationFilterTest {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean
    private JwtUtil jwtUtil;
    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    public void shouldYield401UnauthorizedWhenMissingAuthorizationHeader() throws ServletException, IOException {
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, mock(FilterChain.class));

        verify(response, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    public void shouldYield401UnauthorizedWhenTokenIsInvalid() throws ServletException, IOException {
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        var token = "sometokengoeshere";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.validateTokenAndGetDecoded(token)).thenThrow(JWTVerificationException.class);

        jwtAuthenticationFilter.doFilterInternal(request, response, mock(FilterChain.class));

        verify(response, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    public void shouldYield401UnauthorizedWhenTokenIsEmpty() throws ServletException, IOException {
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer ");

        jwtAuthenticationFilter.doFilterInternal(request, response, mock(FilterChain.class));

        verify(response, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    public void shouldYield401UnauthorizedWhenTokenIsBlank() throws ServletException, IOException {
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer      ");

        jwtAuthenticationFilter.doFilterInternal(request, response, mock(FilterChain.class));

        verify(response, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    public void shouldSucceedWhenTokenIsValid() throws ServletException, IOException {
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        var filterChain = mock(FilterChain.class);
        var user = new User("john.smith@torontomu.ca", "password", UserType.STUDENT);
        var token = "sometokengoeshere";
        var decodedJWT = mock(DecodedJWT.class);
        when(decodedJWT.getSubject()).thenReturn(user.getEmail());
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.validateTokenAndGetDecoded(token)).thenReturn(decodedJWT);
        when(userDetailsService.loadUserByUsername(user.getEmail())).thenReturn(new AppUserDetails(user));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(response, times(0)).setStatus(anyInt());
    }

    @Test
    public void shouldYield401IfTokenValidButUserDoesNotExist() throws ServletException, IOException {
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        var filterChain = mock(FilterChain.class);
        var email = "john.smith@torontomu.ca";
        var token = "sometokengoeshere";
        var decodedJWT = mock(DecodedJWT.class);
        when(decodedJWT.getSubject()).thenReturn(email);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.validateTokenAndGetDecoded(token)).thenReturn(decodedJWT);
        when(userDetailsService.loadUserByUsername(email)).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(response, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

}
