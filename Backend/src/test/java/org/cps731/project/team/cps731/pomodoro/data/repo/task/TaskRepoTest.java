package org.cps731.project.team.cps731.pomodoro.data.repo.task;

import org.cps731.project.team.cps731.pomodoro.data.model.announcement.Announcement;
import org.cps731.project.team.cps731.pomodoro.data.model.assignment.Assignment;
import org.cps731.project.team.cps731.pomodoro.data.model.course.Course;
import org.cps731.project.team.cps731.pomodoro.data.model.course.CourseID;
import org.cps731.project.team.cps731.pomodoro.data.model.course.Term;
import org.cps731.project.team.cps731.pomodoro.data.model.task.Task;
import org.cps731.project.team.cps731.pomodoro.data.model.task.TaskState;
import org.cps731.project.team.cps731.pomodoro.data.model.user.Professor;
import org.cps731.project.team.cps731.pomodoro.data.model.user.Student;
import org.cps731.project.team.cps731.pomodoro.data.model.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
public class TaskRepoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TaskRepo repo;

    @Test
    public void contextLoads(){
        assertThat(repo, is(notNullValue()));
    }

    @Test
    public void findAllByOwnerIsJohnReturnsTasks() {
        var userJohn = new User("John", "password");
        var studentJohn = new Student(userJohn);
        var userSteve = new User("Steve", "password");
        var profSteve = new Professor(userSteve);
        var introToDatabaseSystems = new Course(new CourseID("Intro to database system", Term.FALL, 2024), false, profSteve);
        var announcementForAssignment1 = new Announcement("Announcement 1 is out", Timestamp.from(Instant.now()), "Hello World", introToDatabaseSystems);
        var assignment1 = new Assignment(announcementForAssignment1, Timestamp.from(Instant.now().plus(14, ChronoUnit.DAYS)));
        var assignment1Task = Task.builder()
                .name("Finish Assignment 1")
                .plannedDueDate(Timestamp.from(Instant.now().plus(8, ChronoUnit.DAYS)))
                .state(TaskState.TODO)
                .timeLogged(Duration.of(0, ChronoUnit.MINUTES))
                .pomodorosCompleted(0)
                .owner(studentJohn)
                .derivedFrom(assignment1)
                .build();
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
        entityManager.flush();

        var tasks = repo.findAllByOwnerId(studentJohn.getId());

        assertThat(tasks, equalTo(Set.of(assignment1Task)));
    }

    @Test
    public void findAllByOwnerIsJohnAndStateIsInProgressReturnsEmptySet() {
        var userJohn = new User("John", "password");
        var studentJohn = new Student(userJohn);
        var userSteve = new User("Steve", "password");
        var profSteve = new Professor(userSteve);
        var introToDatabaseSystems = new Course(new CourseID("Intro to database system", Term.FALL, 2024), false, profSteve);
        var announcementForAssignment1 = new Announcement("Announcement 1 is out", Timestamp.from(Instant.now()), "Hello World", introToDatabaseSystems);
        var assignment1 = new Assignment(announcementForAssignment1, Timestamp.from(Instant.now().plus(14, ChronoUnit.DAYS)));
        var assignment1Task = Task.builder()
                .name("Finish Assignment 1")
                .plannedDueDate(Timestamp.from(Instant.now().plus(8, ChronoUnit.DAYS)))
                .state(TaskState.TODO)
                .timeLogged(Duration.of(0, ChronoUnit.MINUTES))
                .pomodorosCompleted(0)
                .owner(studentJohn)
                .derivedFrom(assignment1)
                .build();
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
        entityManager.flush();

        var tasks = repo.findAllByOwnerIdAndStateIsIn(studentJohn.getId(), Set.of(TaskState.IN_PROGRESS));

        assertThat(tasks, empty());
    }

}
