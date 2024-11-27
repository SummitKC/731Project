package org.cps731.project.team.cps731.pomodoro.controllers.student;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.cps731.project.team.cps731.pomodoro.data.model.course.Course;
import org.cps731.project.team.cps731.pomodoro.data.model.user.Student;
import org.cps731.project.team.cps731.pomodoro.dto.*;
import org.cps731.project.team.cps731.pomodoro.security.SecurityUtil;
import org.cps731.project.team.cps731.pomodoro.services.CourseService;
import org.cps731.project.team.cps731.pomodoro.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/student/dashboard")
@PreAuthorize("hasRole('ROLE_STUDENT')")
public class StudentHomePageController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;


    @GetMapping("/courses")
    public ResponseEntity<Set<CourseDTO>> getStudentCourses() {
        var studentId = SecurityUtil.getAuthenticatedUserID();
        Student student = studentService.getStudentById(studentId);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student.getCourses().stream().map(CourseDTO::new).collect(Collectors.toSet()));
    }

    @GetMapping("/tasks")
    public ResponseEntity<Set<TaskDTO>> getUpcomingTasks() {
        var decodedJWT = (DecodedJWT) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        var studentId = Long.parseLong(decodedJWT.getSubject());
        Student student = studentService.getStudentById(studentId);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        Set<TaskDTO> upcomingTasks = student.getTasks().stream().map(TaskDTO::new).collect(Collectors.toSet());
        return ResponseEntity.ok(upcomingTasks);
    }

/*     Try and catch block here would be better cause we would not have to call 
    StudentService to get student by ID, and add course, this can be done in 1 call */
    @PostMapping("/courses/join")
    public ResponseEntity<Void> joinCourse(@RequestBody JoinCourseRequestDTO requestBody) {
        try {
            var studentId = SecurityUtil.getAuthenticatedUserID();
            Course course = courseService.getCourseById(requestBody.getCourseCode());
            if (course == null) {
                return ResponseEntity.notFound().build();
            }
            studentService.addCourseToStudent(studentId, course);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/courses/{courseCode}/details")
    public ResponseEntity<CourseDetailsDTO> getCourseDetails(@PathVariable String courseCode) {
        Course course = courseService.getCourseById(courseCode);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(new CourseDetailsDTO(course));
    }
}