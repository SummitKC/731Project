package org.cps731.project.team.cps731.pomodoro.data.controllers;

import org.cps731.project.team.cps731.pomodoro.data.model.course.Course;
import org.cps731.project.team.cps731.pomodoro.data.model.course.CourseID;
import org.cps731.project.team.cps731.pomodoro.data.model.task.Task;
import org.cps731.project.team.cps731.pomodoro.data.model.task.TaskState;
import org.cps731.project.team.cps731.pomodoro.data.services.CourseService;
import org.cps731.project.team.cps731.pomodoro.data.services.StudentService;
import org.cps731.project.team.cps731.pomodoro.data.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/student/home")
public class StudentHomeController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private TaskService taskService;

    @GetMapping("/{studentId}/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard(@PathVariable Long studentId) {
        // Get student's courses
        Set<Course> courses = studentService.getStudentById(studentId).getCourses();
        
        // Get upcoming tasks
        Timestamp oneWeekAgo = Timestamp.from(Instant.now().minusSeconds(7 * 24 * 60 * 60));
        Set<Task> upcomingTasks = taskService.getTaskByStateAndIssueTime(
            studentId,
            TaskState.TODO,
            oneWeekAgo
        );

        // Get in-progress tasks
        Set<Task> inProgressTasks = taskService.getTaskByState(
            studentId,
            TaskState.IN_PROGRESS
        );

        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("courses", courses);
        dashboard.put("upcomingTasks", upcomingTasks);
        dashboard.put("inProgressTasks", inProgressTasks);

        return ResponseEntity.ok(dashboard);
    }

    @PostMapping("/{studentId}/courses/join")
    public ResponseEntity<Map<String, Object>> joinCourse(
            @PathVariable Long studentId,
            @RequestBody CourseID courseId) {
        
        Course course = courseService.getCourseById(courseId);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }

        if (Boolean.TRUE.equals(course.getArchived())) {
            return ResponseEntity.badRequest().body(
                Map.of("error", "Cannot join archived course")
            );
        }

        studentService.addCourseToStudent(studentId, course);

        return ResponseEntity.ok(Map.of(
            "message", "Successfully joined course",
            "course", course
        ));
    }
}