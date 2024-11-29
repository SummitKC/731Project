package org.cps731.project.team.cps731.pomodoro.controllers.student;

import org.cps731.project.team.cps731.pomodoro.data.model.user.Student;
import org.cps731.project.team.cps731.pomodoro.dto.assignment.AssignmentDTO;
import org.cps731.project.team.cps731.pomodoro.dto.course.CourseDTO;
import org.cps731.project.team.cps731.pomodoro.dto.course.CourseDetailsDTO;
import org.cps731.project.team.cps731.pomodoro.dto.course.JoinCourseRequestDTO;
import org.cps731.project.team.cps731.pomodoro.dto.task.TaskDTO;
import org.cps731.project.team.cps731.pomodoro.security.SecurityUtil;
import org.cps731.project.team.cps731.pomodoro.services.AssignmentService;
import org.cps731.project.team.cps731.pomodoro.services.CourseService;
import org.cps731.project.team.cps731.pomodoro.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/student/dashboard")
@PreAuthorize("hasRole('ROLE_STUDENT')")
public class StudentHomePageController {

    private final StudentService studentService;
    private final CourseService courseService;
    private final AssignmentService assignmentService;

    public StudentHomePageController(StudentService studentService,
                                 CourseService courseService,
                                 AssignmentService assignmentService) {
        this.studentService = studentService;
        this.courseService = courseService;
        this.assignmentService = assignmentService;
    }

    @GetMapping("/courses")
    public ResponseEntity<Set<CourseDTO>> getStudentCourses() {
        var userId = SecurityUtil.getAuthenticatedUserID();
        var courses = courseService.getStudentsCurrentCourses(userId);
        return ResponseEntity.ok(courses.stream().map(CourseDTO::new).collect(Collectors.toSet()));
    }

    @GetMapping("/tasks")
    public ResponseEntity<Set<TaskDTO>> getUpcomingTasks() {
        var studentId = SecurityUtil.getAuthenticatedUserID();
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
        var studentId = SecurityUtil.getAuthenticatedUserID();
        studentService.addCourseToStudent(studentId, requestBody.getCourseCode());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/courses/{courseCode}/details")
    public ResponseEntity<CourseDetailsDTO> getCourseDetails(@PathVariable String courseCode,
                                                             @RequestParam(defaultValue = "10") int assignmentPageSize,
                                                             @RequestParam(defaultValue = "0") int assignmentPageNum
                                                             ) {
        var course = courseService.getCourseById(courseCode);
        var assignments = assignmentService.getAssignmentsByCourse(courseCode, assignmentPageNum, assignmentPageSize);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(new CourseDetailsDTO(course, assignments.stream().map(AssignmentDTO::new).collect(Collectors.toSet())));
    }
}