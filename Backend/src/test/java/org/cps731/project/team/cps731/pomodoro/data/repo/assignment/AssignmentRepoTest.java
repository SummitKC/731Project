package org.cps731.project.team.cps731.pomodoro.data.repo.assignment;

import org.cps731.project.team.cps731.pomodoro.data.model.announcement.Announcement;
import org.cps731.project.team.cps731.pomodoro.data.model.assignment.Assignment;
import org.cps731.project.team.cps731.pomodoro.data.model.course.Course;
import org.cps731.project.team.cps731.pomodoro.data.model.course.Term;
import org.cps731.project.team.cps731.pomodoro.data.model.user.Professor;
import org.cps731.project.team.cps731.pomodoro.data.model.user.User;
import org.cps731.project.team.cps731.pomodoro.data.model.user.UserType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@ActiveProfiles("test")
public class AssignmentRepoTest {

    @Autowired
    private AssignmentRepo repo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void contextLoads(){
        assertThat(repo, is(notNullValue()));
    }

    @Test
    public void findAllByAnnouncement_Course_CourseIDReturnsAllAssignmentsForCourse() {
        var userJohn = new User(1L,"John Smith", "john.smith@torontomu.ca", "password", UserType.PROFESSOR);
        var profJohn = new Professor(userJohn);
        var introToDBSystems = new Course("CPS510", "Introduction To Database Systems", Term.FALL, 2024, false, profJohn);
        var assignment1Announcement = new Announcement("Assignment 1", Timestamp.from(Instant.now()), "Do the thing", introToDBSystems);
        var assignment1 = new Assignment(assignment1Announcement, Timestamp.from(Instant.now().plus(14, ChronoUnit.DAYS)));
        entityManager.persist(userJohn);
        entityManager.persist(profJohn);
        entityManager.persist(introToDBSystems);
        entityManager.persist(assignment1Announcement);
        entityManager.persist(assignment1);
        entityManager.flush();

        var assignments = repo.findAllByAnnouncement_Course_CourseCode(introToDBSystems.getCourseCode(), PageRequest.of(0, 5));

        assertThat(assignments, equalTo(List.of(assignment1)));
    }

}
