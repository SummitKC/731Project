package org.cps731.project.team.cps731.pomodoro.data.repo.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@DataJpaTest
public class ProfessorRepoTest {

    @Autowired
    private ProfessorRepo repo;

    @Test
    public void contextLoads(){
        assertThat(repo, is(notNullValue()));
    }

}