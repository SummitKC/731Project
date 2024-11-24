package org.cps731.project.team.cps731.pomodoro.data.repo.analytics;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@DataJpaTest
@ActiveProfiles("test")
public class WorkAnalyticsRepoTest {

    @Autowired
    private WorkAnalyticsRepo repo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void contextLoads(){
        assertThat(repo, is(notNullValue()));
    }

}
