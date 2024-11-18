package org.cps731.project.team.cps731.pomodoro.data.repo.course;

import org.cps731.project.team.cps731.pomodoro.data.model.course.Course;
import org.cps731.project.team.cps731.pomodoro.data.model.course.CourseID;
import org.cps731.project.team.cps731.pomodoro.data.model.course.Term;
import org.cps731.project.team.cps731.pomodoro.data.model.user.Professor;
import org.cps731.project.team.cps731.pomodoro.data.model.user.Student;
import org.cps731.project.team.cps731.pomodoro.data.model.user.User;
import org.cps731.project.team.cps731.pomodoro.data.repo.CourseRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
public class CourseRepoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CourseRepo repo;

    @Test
    public void contextLoads(){
        assertThat(repo, is(notNullValue()));
    }

    @Test
    public void getCoursesByCreatedByJohnReturnsIntroToDBSystemsAndSoftEng() {
        var userJohn = new User("John", "password");
        var johnCourses = new HashSet<Course>();
        var professorJohn = new Professor(1L, userJohn, johnCourses);
        johnCourses.addAll(List.of(
                new Course(new CourseID("Introduction to Database Systems", Term.FALL, 2024), false, professorJohn, Set.of(), Set.of()),
                new Course(new CourseID("Introduction to Software Engineering", Term.FALL, 2024), false, professorJohn, Set.of(), Set.of())
        ));
        entityManager.persist(userJohn);
        entityManager.persist(professorJohn);
        for(var course : johnCourses) {
            entityManager.persist(course);
        }
        entityManager.flush();

        var courses = repo.findCoursesByCreatedById(userJohn.getId());

        assertThat(courses, equalTo(johnCourses));
    }

    @Test
    public void getCoursesTakeByContainsStudentJohnReturnsIntoToDBSystems() {
        var userJohn = new User("John", "password");
        var userBob = new User("Bob", "password");
        var studentJohn = new Student(userJohn);
        var profBob = new Professor(userBob);
        var introToDatabaseSystems = new Course(new CourseID("Intro to Database Systems", Term.FALL, 2024), false, profBob);
        introToDatabaseSystems.setTakenBy(Set.of(studentJohn));
        introToDatabaseSystems.setCreatedBy(profBob);
        profBob.setCreatedCourses(Set.of(introToDatabaseSystems));
        studentJohn.setCourses(Set.of(introToDatabaseSystems));

        entityManager.persist(userJohn);
        entityManager.persist(userBob);
        entityManager.persist(studentJohn);
        entityManager.persist(profBob);
        entityManager.persist(introToDatabaseSystems);
        entityManager.flush();

        var courses = repo.findCoursesByTakenById(studentJohn.getId());
        assertThat(courses, equalTo(Set.of(introToDatabaseSystems)));
    }

}
