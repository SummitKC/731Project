package org.cps731.project.team.cps731.pomodoro.data.repo.announcement;

import org.cps731.project.team.cps731.pomodoro.data.model.announcement.Announcement;
import org.cps731.project.team.cps731.pomodoro.data.model.course.Course;
import org.cps731.project.team.cps731.pomodoro.data.model.course.CourseID;
import org.cps731.project.team.cps731.pomodoro.data.model.course.Term;
import org.cps731.project.team.cps731.pomodoro.data.model.user.Professor;
import org.cps731.project.team.cps731.pomodoro.data.model.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@ActiveProfiles("test")
public class AnnouncementRepoTest {

    @Autowired
    private AnnouncementRepo repo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void contextLoads(){
        assertThat(repo, is(notNullValue()));
    }

    @Test
    public void findAllByCourse_CourseIDReturnsAllAnnouncementsForCourse() {
        var userJohn = new User("John", "password");
        var profJohn = new Professor(userJohn);
        var introToDBSystems = new Course(new CourseID("Introduction To DatabaseSystems", Term.FALL, 2024), false, profJohn);
        var startOfCourseAnnouncement = new Announcement("Welcome to the course", Timestamp.from(Instant.now()), "Hiii", introToDBSystems);
        entityManager.persist(userJohn);
        entityManager.persist(profJohn);
        entityManager.persist(introToDBSystems);
        entityManager.persist(startOfCourseAnnouncement);
        entityManager.flush();

        var announcements = repo.findAllByCourse_CourseID(introToDBSystems.getCourseID(), PageRequest.of(0, 5));

        assertThat(announcements, equalTo(List.of(startOfCourseAnnouncement)));
    }

}
