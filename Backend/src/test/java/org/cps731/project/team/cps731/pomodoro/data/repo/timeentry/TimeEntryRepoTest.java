package org.cps731.project.team.cps731.pomodoro.data.repo.timeentry;

import org.cps731.project.team.cps731.pomodoro.data.model.announcement.Announcement;
import org.cps731.project.team.cps731.pomodoro.data.model.assignment.Assignment;
import org.cps731.project.team.cps731.pomodoro.data.model.course.Course;
import org.cps731.project.team.cps731.pomodoro.data.model.course.CourseID;
import org.cps731.project.team.cps731.pomodoro.data.model.course.Term;
import org.cps731.project.team.cps731.pomodoro.data.model.task.Task;
import org.cps731.project.team.cps731.pomodoro.data.model.task.TaskState;
import org.cps731.project.team.cps731.pomodoro.data.model.timeentry.TimeEntry;
import org.cps731.project.team.cps731.pomodoro.data.model.user.Professor;
import org.cps731.project.team.cps731.pomodoro.data.model.user.Student;
import org.cps731.project.team.cps731.pomodoro.data.model.user.User;
import org.cps731.project.team.cps731.pomodoro.data.model.user.UserType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@ActiveProfiles("test")
public class TimeEntryRepoTest {

    @Autowired
    private TimeEntryRepo repo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void contextLoads(){
        assertThat(repo, is(notNullValue()));
    }

    @Test
    public void findAllByOwnerIdAndStartTimeBeforeReturnsAllStudentTimeEntriesBeforeTime() {
        var userJohn = new User("John", "password", UserType.STUDENT);
        var studentJohn = new Student(userJohn);
        var userSteve = new User("Steve", "password", UserType.PROFESSOR);
        var profSteve = new Professor(userSteve);
        var introToDatabaseSystems = new Course(new CourseID("Intro to database system", Term.FALL, 2024), false, profSteve);
        var announcementForAssignment1 = new Announcement("Announcement 1 is out", Timestamp.from(Instant.now()), "Hello World", introToDatabaseSystems);
        var oneWeekAgo = Instant.now().minus(7, ChronoUnit.DAYS);
        var twoWeeksAgo = Instant.now().minus(14, ChronoUnit.DAYS);
        var assignment1 = new Assignment(announcementForAssignment1, Timestamp.from(twoWeeksAgo));
        var assignment1Task = Task.builder()
                .name("Finish Assignment 1")
                .plannedDueDate(Timestamp.from(Instant.now().plus(8, ChronoUnit.DAYS)))
                .state(TaskState.TODO)
                .owner(studentJohn)
                .derivedFrom(assignment1)
                .build();
        var timeEntry1 = new TimeEntry(assignment1Task, Timestamp.from(twoWeeksAgo), Timestamp.from(twoWeeksAgo.plus(25, ChronoUnit.MINUTES)), 1);
        var sixDaysAgo = Instant.now().minus(6, ChronoUnit.DAYS);
        var timeEntry2 = new TimeEntry(assignment1Task, Timestamp.from(sixDaysAgo), Timestamp.from(sixDaysAgo.plus(25, ChronoUnit.MINUTES)), 2);
        studentJohn.setCourses(Set.of(introToDatabaseSystems));
        studentJohn.setTasks(Set.of(assignment1Task));
        profSteve.setCreatedCourses(Set.of(introToDatabaseSystems));
        introToDatabaseSystems.setAnnouncements(Set.of(announcementForAssignment1));
        entityManager.persist(userJohn);
        entityManager.persist(studentJohn);
        entityManager.persist(userSteve);
        entityManager.persist(profSteve);
        entityManager.persist(introToDatabaseSystems);
        entityManager.persist(announcementForAssignment1);
        entityManager.persist(assignment1);
        entityManager.persist(assignment1Task);
        entityManager.persist(timeEntry1);
        entityManager.persist(timeEntry2);
        entityManager.flush();

        var timeEntriesBeforeAWeekAgo = repo.findAllByTask_OwnerIdAndStartTimeAfter(studentJohn.getId(), Timestamp.from(oneWeekAgo));

        assertThat(timeEntriesBeforeAWeekAgo, equalTo(Set.of(timeEntry2)));
    }

}
