package org.cps731.project.team.cps731.pomodoro.controllers.student;

import org.cps731.project.team.cps731.pomodoro.data.model.task.TaskState;
import org.cps731.project.team.cps731.pomodoro.dto.*;
import org.cps731.project.team.cps731.pomodoro.dto.course.CourseDTO;
import org.cps731.project.team.cps731.pomodoro.dto.course.JoinCourseRequestDTO;
import org.cps731.project.team.cps731.pomodoro.dto.student.StudentDashboardDTO;
import org.cps731.project.team.cps731.pomodoro.dto.student.StudentProfileDTO;
import org.cps731.project.team.cps731.pomodoro.dto.task.TaskDTO;
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

    private final StudentService studentService;
    private final CourseService courseService;
    private final TaskService taskService;

    @Autowired
    public StudentHomeController(StudentService studentService, CourseService courseService, TaskService taskService) {
        this.studentService = studentService;
        this.courseService = courseService;
        this.taskService = taskService;
    }

    @GetMapping("/profile")
    public ResponseEntity<StudentProfileDTO> getProfile() {
        return ResponseEntity.ok(studentService.getStudentProfile(SecurityUtil.getAuthenticatedUserID()));
    }

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
        Set<TaskDTO> inProgressTasks = taskService.getAllTasksByState(
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

        studentService.addCourseToStudent(studentId, body.getCourseCode());

        return ResponseEntity.noContent().build();
    }
}