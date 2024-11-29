package org.cps731.project.team.cps731.pomodoro.data.repo.course;

import org.cps731.project.team.cps731.pomodoro.data.model.course.Course;
import org.cps731.project.team.cps731.pomodoro.data.model.course.Term;
import org.cps731.project.team.cps731.pomodoro.data.model.user.Professor;
import org.cps731.project.team.cps731.pomodoro.data.model.user.Student;
import org.cps731.project.team.cps731.pomodoro.data.model.user.User;
import org.cps731.project.team.cps731.pomodoro.data.model.user.UserType;
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
        var userJohn = new User("John Smith", "john.smith@torontomu.ca", "password", UserType.PROFESSOR);
        var johnCourses = new HashSet<Course>();
        entityManager.persist(userJohn);
        var professorJohn = new Professor(userJohn.getId(), 1L, userJohn, johnCourses);
        johnCourses.addAll(List.of(
                new Course("CPS510", "Introduction to Database Systems", Term.FALL, 2024, false, professorJohn, Set.of(), Set.of()),
                new Course("CPS406", "Introduction to Software Engineering", Term.FALL, 2024, false, professorJohn, Set.of(), Set.of())
        ));
        entityManager.persist(professorJohn);
        for(var course : johnCourses) {
            entityManager.persist(course);
        }
        entityManager.flush();

        var courses = repo.findCoursesByCreatedByUserID(userJohn.getId());

        assertThat(courses, equalTo(johnCourses));
    }

    @Test
    public void getCoursesTakeByContainsStudentJohnReturnsIntoToDBSystems() {
        var userJohn = new User("John", "john.smith@torontomu.ca", "password", UserType.STUDENT);
        var userBob = new User("Bob", "bob.smith@torontomu.ca", "password", UserType.PROFESSOR);
        var studentJohn = new Student(userJohn, 1L);
        var profBob = new Professor(userBob, 2L);
        var introToDatabaseSystems = new Course("CPS510", "Intro to Database Systems", Term.FALL, 2024, false, profBob);
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

        var courses = repo.findCoursesByTakenByID(studentJohn.getID());
        assertThat(courses, equalTo(Set.of(introToDatabaseSystems)));
    }

}
