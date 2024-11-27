package org.cps731.project.team.cps731.pomodoro.controllers.student;

import org.cps731.project.team.cps731.pomodoro.data.model.course.Course;
import org.cps731.project.team.cps731.pomodoro.data.model.task.TaskState;
import org.cps731.project.team.cps731.pomodoro.dto.*;
import org.cps731.project.team.cps731.pomodoro.security.SecurityUtil;
import org.cps731.project.team.cps731.pomodoro.services.CourseService;
import org.cps731.project.team.cps731.pomodoro.services.StudentService;
import org.cps731.project.team.cps731.pomodoro.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/student/home")
@PreAuthorize("hasRole('ROLE_STUDENT')")
public class StudentHomeController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private TaskService taskService;

    @GetMapping("/dashboard")
    public ResponseEntity<StudentDashboardDTO> getDashboard() {
        // Get student's courses
        var studentId = SecurityUtil.getAuthenticatedUserID();
        Set<CourseDTO> courses = studentService.getStudentById(studentId).getCourses().stream().map(CourseDTO::new).collect(Collectors.toSet());
        
        // Get upcoming tasks
        Timestamp oneWeekAgo = Timestamp.from(Instant.now().minus(7, ChronoUnit.DAYS));
        Set<TaskDTO> upcomingTasks = taskService.getTaskByStateAndIssueTime(
            studentId,
            TaskState.TODO,
            oneWeekAgo
        ).stream().map(TaskDTO::new).collect(Collectors.toSet());

        // Get in-progress tasks
        Set<TaskDTO> inProgressTasks = taskService.getTaskByState(
            studentId,
            TaskState.IN_PROGRESS
        ).stream().map(TaskDTO::new).collect(Collectors.toSet());

        return ResponseEntity.ok(new StudentDashboardDTO(
                courses,
                upcomingTasks,
                inProgressTasks
        ));
    }

    @PostMapping("/courses/join")
    public ResponseEntity<StatusDTO> joinCourse(
            @RequestBody JoinCourseRequestDTO body) {
        var studentId = SecurityUtil.getAuthenticatedUserID();
        Course course = courseService.getCourseById(body.getCourseCode());
        if (course == null) {
            return ResponseEntity.notFound().build();
        }

        if (Boolean.TRUE.equals(course.getArchived())) {
            return ResponseEntity.badRequest().body(new StatusDTO(
                    "error",
                    "Cannot join an archived course"
            ));
        }

        studentService.addCourseToStudent(studentId, course);

        return ResponseEntity.ok(new StatusDTO(
                "success",
                "Successfully joined course"
        ));
    }
}