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
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {JwtAuthenticationFilter.class, SecurityContext.class})
public class JWTAuthenticationFilterTest {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean
    private JwtUtil jwtUtil;
    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    public void shouldFailWhenMissingAuthorizationHeader() throws ServletException, IOException {
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, mock(FilterChain.class));

        assertThat(SecurityContextHolder.getContext().getAuthentication(), is(nullValue()));
    }

    @Test
    public void shouldFailWhenTokenIsInvalid() throws ServletException, IOException {
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        var token = "sometokengoeshere";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.validateTokenAndGetDecoded(token)).thenThrow(JWTVerificationException.class);

        jwtAuthenticationFilter.doFilterInternal(request, response, mock(FilterChain.class));

        assertThat(SecurityContextHolder.getContext().getAuthentication(), is(nullValue()));
    }

    @Test
    public void shouldFailWhenTokenIsEmpty() throws ServletException, IOException {
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer ");

        jwtAuthenticationFilter.doFilterInternal(request, response, mock(FilterChain.class));

        assertThat(SecurityContextHolder.getContext().getAuthentication(), is(nullValue()));
    }

    @Test
    public void shouldFailWhenTokenIsBlank() throws ServletException, IOException {
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer      ");

        jwtAuthenticationFilter.doFilterInternal(request, response, mock(FilterChain.class));

        assertThat(SecurityContextHolder.getContext().getAuthentication(), is(nullValue()));
    }

    @Test
    public void shouldSucceedWhenTokenIsValid() throws ServletException, IOException {
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        var filterChain = mock(FilterChain.class);
        var userID = 1L;
        var user = new User("john.smith@torontomu.ca", "password", UserType.STUDENT);
        user.setId(userID);
        var token = "sometokengoeshere";
        var decodedJWT = mock(DecodedJWT.class);
        when(decodedJWT.getSubject()).thenReturn(user.getEmail());
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.validateTokenAndGetDecoded(token)).thenReturn(decodedJWT);
        when(userDetailsService.loadUserByUsername(user.getEmail())).thenReturn(new AppUserDetails(user));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication(), is(not(nullValue())));
    }

    @Test
    public void shouldFailIfTokenValidButUserDoesNotExist() throws ServletException, IOException {
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

        assertThat(SecurityContextHolder.getContext().getAuthentication(), is(nullValue()));
    }

}
