package org.cps731.project.team.cps731.pomodoro.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static Long getAuthenticatedUserID() {
        var decodedJWT = (DecodedJWT) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        return Long.parseLong(decodedJWT.getSubject());
    }

}
