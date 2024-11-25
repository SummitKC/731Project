package org.cps731.project.team.cps731.pomodoro.security.principal;

import org.cps731.project.team.cps731.pomodoro.data.model.user.User;
import org.cps731.project.team.cps731.pomodoro.data.model.user.UserType;
import org.cps731.project.team.cps731.pomodoro.data.repo.user.UserRepo;
import org.cps731.project.team.cps731.pomodoro.security.principal.authority.AppAuthorities;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {AppUserDetailsService.class})
public class AppUserDetailsServiceTest {

    @MockBean
    private UserRepo userRepo;
    @Autowired
    private AppUserDetailsService userDetailsService;

    @Test
    public void shouldReturnAppUserDetailsWithStudentRoleForStudentsEmail() {
        var user = new User("john.smith@torontomu.ca", "password", UserType.STUDENT);
        when(userRepo.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        var userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        assertThat(userDetails.getUsername(), equalTo(user.getEmail()));
        assertThat(userDetails.getPassword(), equalTo(user.getPassword()));
        assertThat(userDetails.getAuthorities(), equalTo(Set.of(AppAuthorities.STUDENT)));
    }

    @Test
    public void shouldReturnAppUserDetailsWithProfessorRoleForProfessorsEmail() {
        var user = new User("jane.smith@torontomu.ca", "password", UserType.PROFESSOR);
        when(userRepo.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        var userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        assertThat(userDetails.getUsername(), equalTo(user.getEmail()));
        assertThat(userDetails.getPassword(), equalTo(user.getPassword()));
        assertThat(userDetails.getAuthorities(), equalTo(Set.of(AppAuthorities.PROFESSOR)));
    }

    @Test
    public void shouldReturnNullForNonExistentEmail() {
        var email = "fake.guy@torontomu.ca";
        when(userRepo.findByEmail(email)).thenReturn(Optional.empty());

        var userDetails = userDetailsService.loadUserByUsername(email);

        assertThat(userDetails, nullValue());
    }

}
