package org.cps731.project.team.cps731.pomodoro.data.controllers;

import org.cps731.project.team.cps731.pomodoro.data.model.course.Course;
import org.cps731.project.team.cps731.pomodoro.data.model.course.CourseID;
import org.cps731.project.team.cps731.pomodoro.data.model.task.Task;
import org.cps731.project.team.cps731.pomodoro.data.model.user.Student;
import org.cps731.project.team.cps731.pomodoro.data.services.CourseService;
import org.cps731.project.team.cps731.pomodoro.data.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;


@RestController
@RequestMapping("/api/student/taskboard")
public class StudentHomePageController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;



    @GetMapping("/courses/{studentId}")
    public ResponseEntity<Set<Course>> getStudentCourses(@PathVariable Long studentId) {
        Student student = studentService.getStudentById(studentId);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student.getCourses());
    }

    @GetMapping("/tasks/{studentId}")
    public ResponseEntity<Set<Task>> getUpcomingTasks(@PathVariable Long studentId) {
        Student student = studentService.getStudentById(studentId);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        Set<Task> upcomingTasks = student.getTasks();
        return ResponseEntity.ok(upcomingTasks);
    }

/*     Try and catch block here would be better cause we would not have to call 
    StudentService to get student by ID, and add course, this can be done in 1 call */
    @PostMapping("/courses/{studentId}/join")
    public ResponseEntity<Student> joinCourse(@PathVariable Long studentId, @PathVariable CourseID courseId) {
        try {
            Course course = courseService.getCourseById(courseId);
            if (course == null) {
                return ResponseEntity.notFound().build();
            }
            
            Student updatedStudent = studentService.addCourseToStudent(studentId, course);
            return ResponseEntity.ok(updatedStudent);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/courses/{courseId}/details")
    public ResponseEntity<Map<String, Object>> getCourseDetails(@PathVariable CourseID courseId) {
        Course course = courseService.getCourseById(courseId);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }
        
        Map<String, Object> courseDetails = Map.of(
            "courseId", course.getCourseID(),
            "professor", course.getCreatedBy().getUser().getUsername(),
            "announcements", course.getAnnouncements(),
            "isArchived", course.getArchived()
        );
        
        return ResponseEntity.ok(courseDetails);
    }
}