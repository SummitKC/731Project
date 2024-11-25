package org.cps731.project.team.cps731.pomodoro.data;

import jakarta.persistence.EntityManager;
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
import org.cps731.project.team.cps731.pomodoro.data.model.user.UserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;

@Component
@Profile({"dev", "mysqldev"})
public class DevDataInitializer implements ApplicationRunner {

    private final EntityManager entityManager;
    private static final Logger LOG = LoggerFactory.getLogger(DevDataInitializer.class);

    @Autowired
    public DevDataInitializer(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        LOG.info("Initializing DEV Data");

        var userJohn = new User("john.smith@torontomu.ca", "$2y$10$ZZlD.z6h6dnc03RSzqaLwOkJ5Ea1rHoe0l3mZCYMtll9QZvKV8kkO", UserType.STUDENT);
        var studentJohn = new Student(userJohn);
        var userSummit = new User("summit.smith@torontomu.ca", "$2y$10$ZZlD.z6h6dnc03RSzqaLwOkJ5Ea1rHoe0l3mZCYMtll9QZvKV8kkO", UserType.PROFESSOR);
        var profSummit = new Professor(userSummit);
        var introToDbSystems = new Course(new CourseID("Intro To Database Systems", Term.FALL, 2024), false, profSummit);
        var introToSoftEng = new Course(new CourseID("Intro To Software Engineering", Term.FALL, 2023), true, profSummit);
        var announcementForA1 = Announcement.builder()
                .title("Assignment 1")
                .description("Assignment 1 is out now!")
                .course(introToDbSystems)
                .issueTime(Timestamp.from(Instant.now()))
                .build();
        var a1 = Assignment.builder()
                        .announcement(announcementForA1)
                        .dueDate(Timestamp.from(Instant.now().plus(14, ChronoUnit.DAYS)))
                        .build();
        var task1 = Task.builder()
                .owner(studentJohn)
                .state(TaskState.IN_PROGRESS)
                .name("Finish Assignment 1")
                .plannedDueDate(Timestamp.from(Instant.now().plus(10, ChronoUnit.DAYS)))
                .derivedFrom(a1)
                .build();

        a1.setDerivingTasks(Set.of(task1));
        introToDbSystems.setAnnouncements(Set.of(announcementForA1));
        profSummit.setCreatedCourses(Set.of(introToDbSystems, introToSoftEng));

        entityManager.persist(userJohn);
        entityManager.persist(userSummit);
        entityManager.persist(studentJohn);
        entityManager.persist(profSummit);
        entityManager.persist(introToDbSystems);
        entityManager.persist(introToSoftEng);
        entityManager.persist(announcementForA1);
        entityManager.persist(a1);
        entityManager.persist(task1);
        entityManager.flush();
    }
}
