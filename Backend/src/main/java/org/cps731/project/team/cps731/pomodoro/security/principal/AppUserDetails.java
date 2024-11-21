package org.cps731.project.team.cps731.pomodoro.security.principal;

import org.cps731.project.team.cps731.pomodoro.data.model.user.User;
import org.cps731.project.team.cps731.pomodoro.security.principal.authority.AppAuthorities;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

public class AppUserDetails implements UserDetails {

    private final String password;
    private final String email;
    private final Set<AppAuthorities> authorities;

    public AppUserDetails(User user) {
        email = user.getEmail();
        password = user.getPassword();
        authorities = switch (user.getUserType()) {
            case STUDENT: yield Set.of(AppAuthorities.STUDENT);
            case PROFESSOR: yield Set.of(AppAuthorities.PROFESSOR);
            case null: yield Set.of();
        };
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
