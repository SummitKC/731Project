package org.cps731.project.team.cps731.pomodoro.data.repo;

import org.cps731.project.team.cps731.pomodoro.data.model.course.Course;
import org.cps731.project.team.cps731.pomodoro.data.model.course.CourseID;
import org.cps731.project.team.cps731.pomodoro.data.model.user.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CourseRepo extends JpaRepository<Course, CourseID> {

    Set<Course> findCoursesByTakenById(Long takenById);
    Set<Course> findCoursesByCreatedById(Long id);

}
