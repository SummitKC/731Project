package org.cps731.project.team.cps731.pomodoro.security.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.cps731.project.team.cps731.pomodoro.security.auth.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            String authorizationHeader = request.getHeader("Authorization");

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                if (token != null && !token.isBlank() && SecurityContextHolder.getContext().getAuthentication() == null) {
                    try {
                        var decodedToken = jwtUtil.validateTokenAndGetDecoded(token);
                        var idString = decodedToken.getSubject();
                        var userDetails = userDetailsService.loadUserByUsername(idString);
                        if (userDetails == null) {
                            filterChain.doFilter(request, response);
                            return;
                        }
                        var authToken = new UsernamePasswordAuthenticationToken(
                                userDetails, decodedToken, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    } catch (JWTVerificationException ex) {
                        filterChain.doFilter(request, response);
                        return;
                    }
                }
            }
            filterChain.doFilter(request, response);
    }
}