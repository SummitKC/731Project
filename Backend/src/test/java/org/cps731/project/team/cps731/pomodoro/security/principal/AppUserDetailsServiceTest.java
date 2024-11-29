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
    public void shouldReturnAppUserDetailsWithStudentRoleForStudentsID() {
        var userID = 1L;
        var user = new User( "John", "john.smith@torontomu.ca", "password", UserType.STUDENT);
        user.setId(userID);
        when(userRepo.findById(userID))
                .thenReturn(Optional.of(user));

        var userDetails = userDetailsService.loadUserByUsername(userID + "");

        assertThat(userDetails.getUsername(), equalTo(user.getId() + ""));
        assertThat(userDetails.getPassword(), equalTo(user.getPassword()));
        assertThat(userDetails.getAuthorities(), equalTo(Set.of(AppAuthorities.STUDENT)));
    }

    @Test
    public void shouldReturnAppUserDetailsWithProfessorRoleForProfessorsID() {
        var userID = 1L;
        var user = new User("Jane Smith", "jane.smith@torontomu.ca", "password", UserType.PROFESSOR);
        user.setId(userID);
        when(userRepo.findById(userID))
                .thenReturn(Optional.of(user));

        var userDetails = userDetailsService.loadUserByUsername(userID + "");

        assertThat(userDetails.getUsername(), equalTo(user.getId() + ""));
        assertThat(userDetails.getPassword(), equalTo(user.getPassword()));
        assertThat(userDetails.getAuthorities(), equalTo(Set.of(AppAuthorities.PROFESSOR)));
    }

    @Test
    public void shouldReturnNullForNonExistentID() {
        var idString = "1";
        when(userRepo.findByEmail(idString)).thenReturn(Optional.empty());

        var userDetails = userDetailsService.loadUserByUsername(idString);

        assertThat(userDetails, nullValue());
    }

}
