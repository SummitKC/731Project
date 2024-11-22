package org.cps731.project.team.cps731.pomodoro.Services;

import org.cps731.project.team.cps731.pomodoro.data.model.course.Course;
import org.cps731.project.team.cps731.pomodoro.data.model.course.CourseID;
import org.cps731.project.team.cps731.pomodoro.data.repo.course.CourseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepo courseRepo;

    //Create coures
    public Course createCourse(Course course) {
        return courseRepo.save(course);
    }

    // Find by ID 
    public Optional<Course> getCourseById(CourseID id) {
        return courseRepo.findById(id);
    }

    // Get all Courses
    public List<Course> getAllCourses() {
        return courseRepo.findAll();
    }

    // Update Course, Pass in through a similar form used to create a new course, extract the course details from the Course object and update
    public Course updateCourse(CourseID id, Course courseDetails) {
        Course course = courseRepo.findById(id).orElseThrow();
        course.setArchived(courseDetails.getArchived());
        course.setCreatedBy(courseDetails.getCreatedBy());
        course.setTakenBy(courseDetails.getTakenBy());
        course.setAnnouncements(courseDetails.getAnnouncements());
        return courseRepo.save(course);
    }

    // Delete Course
    public void deleteCourse(CourseID id) {
        courseRepo.deleteById(id);
    }
}
