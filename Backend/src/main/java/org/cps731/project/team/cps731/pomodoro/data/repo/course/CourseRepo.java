package org.cps731.project.team.cps731.pomodoro.data.repo.course;

import org.cps731.project.team.cps731.pomodoro.data.model.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CourseRepo extends JpaRepository<Course, String> {

    Set<Course> findCoursesByTakenByID(Long takenById);
    Set<Course> findCoursesByCreatedByUserID(Long id);

}
