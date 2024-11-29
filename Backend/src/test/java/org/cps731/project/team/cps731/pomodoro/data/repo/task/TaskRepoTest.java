package org.cps731.project.team.cps731.pomodoro.data.repo.task;

import org.cps731.project.team.cps731.pomodoro.data.model.announcement.Announcement;
import org.cps731.project.team.cps731.pomodoro.data.model.assignment.Assignment;
import org.cps731.project.team.cps731.pomodoro.data.model.course.Course;
import org.cps731.project.team.cps731.pomodoro.data.model.course.Term;
import org.cps731.project.team.cps731.pomodoro.data.model.task.Task;
import org.cps731.project.team.cps731.pomodoro.data.model.task.TaskState;
import org.cps731.project.team.cps731.pomodoro.data.model.user.Professor;
import org.cps731.project.team.cps731.pomodoro.data.model.user.Student;
import org.cps731.project.team.cps731.pomodoro.data.model.user.User;
import org.cps731.project.team.cps731.pomodoro.data.model.user.UserType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.sql.Timestamp;
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
        var userJohn = new User(1L, "John", "john.smith@torontomu.ca", "password", UserType.STUDENT);
        var studentJohn = new Student(userJohn);
        var userSteve = new User(2L, "Steve", "steve.smith@torontomu.ca", "password", UserType.PROFESSOR);
        var profSteve = new Professor(userSteve);
        var introToDatabaseSystems = new Course("CPS510", "Intro to database system", Term.FALL, 2024, false, profSteve);
        var announcementForAssignment1 = new Announcement("Announcement 1 is out", Timestamp.from(Instant.now()), "Hello World", introToDatabaseSystems);
        var assignment1 = new Assignment(announcementForAssignment1, Timestamp.from(Instant.now().plus(14, ChronoUnit.DAYS)));
        var assignment1Task = Task.builder()
                .name("Finish Assignment 1")
                .plannedDueDate(Timestamp.from(Instant.now().plus(8, ChronoUnit.DAYS)))
                .state(TaskState.TODO)
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

        var tasks = repo.findAllByOwnerStudentID(studentJohn.getStudentID());

        assertThat(tasks, equalTo(Set.of(assignment1Task)));
    }

    @Test
    public void findAllByOwnerIsJohnAndStateIsInProgressReturnsEmptySet() {
        var userJohn = new User(1L, "John", "john.smith@torontomu.ca", "password", UserType.STUDENT);
        var studentJohn = new Student(userJohn);
        var userSteve = new User(2L, "Steve", "steve.smith@torontomu.ca", "password", UserType.PROFESSOR);
        var profSteve = new Professor(userSteve);
        var introToDatabaseSystems = new Course("CPS510", "Intro to database system", Term.FALL, 2024, false, profSteve);
        var announcementForAssignment1 = new Announcement("Announcement 1 is out", Timestamp.from(Instant.now()), "Hello World", introToDatabaseSystems);
        var assignment1 = new Assignment(announcementForAssignment1, Timestamp.from(Instant.now().plus(14, ChronoUnit.DAYS)));
        var assignment1Task = Task.builder()
                .name("Finish Assignment 1")
                .plannedDueDate(Timestamp.from(Instant.now().plus(8, ChronoUnit.DAYS)))
                .state(TaskState.TODO)
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

        var tasks = repo.findAllByOwnerStudentIDAndStateIsIn(studentJohn.getStudentID(), Set.of(TaskState.IN_PROGRESS));

        assertThat(tasks, empty());
    }

    @Test
    public void findAllByOwnerIsJohnAndStateIsInProgressToDoReviewingOrDoneAndAssignmentIssueTimeBeforeOneMonthAgoReturnsJohnsAssignmentsFromThePastMonth() {
        var userJohn = new User(1L, "John", "john.smith@torontomu.ca", "password", UserType.STUDENT);
        var studentJohn = new Student(userJohn);
        var userSteve = new User(2L, "Steve", "steve.smith@torontomu.ca", "password", UserType.PROFESSOR);
        var profSteve = new Professor(userSteve);
        var introToDatabaseSystems = new Course("CPS510", "Intro to database system", Term.FALL, 2024, false, profSteve);
        var announcementForAssignment1 = new Announcement("Announcement 1 is out", Timestamp.from(Instant.now().minus(50, ChronoUnit.DAYS)), "Hello World", introToDatabaseSystems);
        var assignment1 = new Assignment(announcementForAssignment1, Timestamp.from(Instant.now().minus(14, ChronoUnit.DAYS)));
        var assignment1Task = Task.builder()
                .name("Finish Assignment 1")
                .plannedDueDate(Timestamp.from(Instant.now().minus(14, ChronoUnit.DAYS)))
                .state(TaskState.COMPLETE)
                .owner(studentJohn)
                .derivedFrom(assignment1)
                .build();
        var announcementForAssignment2 = new Announcement("Announcement 2 is out", Timestamp.from(Instant.now().minus(20, ChronoUnit.DAYS)), "Hello World", introToDatabaseSystems);
        var assignment2 = new Assignment(announcementForAssignment2, Timestamp.from(Instant.now().minus(2, ChronoUnit.DAYS)));
        var assignment2Task = Task.builder()
                .name("Finish Assignment 2")
                .plannedDueDate(Timestamp.from(Instant.now().minus(2, ChronoUnit.DAYS)))
                .state(TaskState.COMPLETE)
                .owner(studentJohn)
                .derivedFrom(assignment2)
                .build();
        var announcementForAssignment3 = new Announcement("Announcement 3 is out", Timestamp.from(Instant.now().minus(7, ChronoUnit.DAYS)), "Hello World", introToDatabaseSystems);
        var assignment3 = new Assignment(announcementForAssignment3, Timestamp.from(Instant.now().plus(3, ChronoUnit.DAYS)));
        var assignment3Task = Task.builder()
                .name("Finish Assignment 3")
                .plannedDueDate(Timestamp.from(Instant.now().plus(1, ChronoUnit.DAYS)))
                .state(TaskState.IN_PROGRESS)
                .owner(studentJohn)
                .derivedFrom(assignment3)
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
        entityManager.persist(announcementForAssignment2);
        entityManager.persist(assignment2);
        entityManager.persist(assignment2Task);
        entityManager.persist(announcementForAssignment3);
        entityManager.persist(assignment3);
        entityManager.persist(assignment3Task);
        entityManager.flush();

        var tasks = repo.findAllByOwnerStudentIDAndStateIsInAndDerivedFrom_Announcement_IssueTimeAfter(studentJohn.getStudentID(), Set.of(TaskState.TODO, TaskState.IN_PROGRESS, TaskState.REVIEWING, TaskState.COMPLETE), Timestamp.from(Instant.now().minus(30, ChronoUnit.DAYS)));

        assertThat(tasks, equalTo(Set.of(assignment2Task, assignment3Task)));
    }

}
