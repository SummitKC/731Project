package org.cps731.project.team.cps731.pomodoro.security.principal;

import org.cps731.project.team.cps731.pomodoro.data.model.user.User;
import org.cps731.project.team.cps731.pomodoro.security.principal.authority.AppAuthorities;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

public class AppUserDetails implements UserDetails {

    private final String password;
    private final String idString;
    private final Collection<AppAuthorities> authorities;

    public AppUserDetails(User user) {
        idString = user.getId().toString();
        password = user.getPassword();
        authorities = switch (user.getUserType()) {
            case STUDENT:
                yield Set.of(AppAuthorities.STUDENT);
            case PROFESSOR:
                yield Set.of(AppAuthorities.PROFESSOR);
            case null:
                yield Set.of();
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
        return idString;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof AppUserDetails)) return false;
        final AppUserDetails other = (AppUserDetails) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$password = this.getPassword();
        final Object other$password = other.getPassword();
        if (this$password == null ? other$password != null : !this$password.equals(other$password)) return false;
        final Object this$email = this.idString;
        final Object other$email = other.idString;
        if (this$email == null ? other$email != null : !this$email.equals(other$email)) return false;
        final Object this$authorities = this.getAuthorities();
        final Object other$authorities = other.getAuthorities();
        if (this$authorities == null ? other$authorities != null : !this$authorities.equals(other$authorities))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof AppUserDetails;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $password = this.getPassword();
        result = result * PRIME + ($password == null ? 43 : $password.hashCode());
        final Object $email = this.idString;
        result = result * PRIME + ($email == null ? 43 : $email.hashCode());
        final Object $authorities = this.getAuthorities();
        result = result * PRIME + ($authorities == null ? 43 : $authorities.hashCode());
        return result;
    }
}
