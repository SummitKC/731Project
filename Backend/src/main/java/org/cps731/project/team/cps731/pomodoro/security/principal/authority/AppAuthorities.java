package org.cps731.project.team.cps731.pomodoro.security.principal.authority;

import org.springframework.security.core.GrantedAuthority;

public enum AppAuthorities implements GrantedAuthority {
    STUDENT, PROFESSOR;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}
