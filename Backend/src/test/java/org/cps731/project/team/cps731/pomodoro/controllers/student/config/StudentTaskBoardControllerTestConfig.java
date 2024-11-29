package org.cps731.project.team.cps731.pomodoro.controllers.student.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
public class StudentTaskBoardControllerTestConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/**").permitAll()
                    .requestMatchers(HttpMethod.POST).permitAll();
                })
                .csrf(AbstractHttpConfigurer::disable).build();
    }

}
