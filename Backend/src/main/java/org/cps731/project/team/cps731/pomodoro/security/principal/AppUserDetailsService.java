package org.cps731.project.team.cps731.pomodoro.security.principal;

import org.cps731.project.team.cps731.pomodoro.data.repo.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    @Autowired
    public AppUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String idString) throws UsernameNotFoundException {
        var userOpt = userRepo.findById(Long.parseLong(idString));
        return userOpt.map(AppUserDetails::new).orElse(null);
    }
}
