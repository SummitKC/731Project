package org.cps731.project.team.cps731.pomodoro.controllers.student;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.cps731.project.team.cps731.pomodoro.data.model.task.Task;
import org.cps731.project.team.cps731.pomodoro.data.model.task.TaskState;
import org.cps731.project.team.cps731.pomodoro.security.auth.JwtUtil;
import org.cps731.project.team.cps731.pomodoro.services.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/student/taskboard")
public class StudentTaskBoardController {

    private final TaskService taskService;
    private final JwtUtil jwtUtil;

    public StudentTaskBoardController(TaskService taskService, JwtUtil jwtUtil) {
        this.taskService = taskService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/tasks")
    public ResponseEntity<Map<String, Object>> getTaskBoard(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        var decodedJWT = (DecodedJWT) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        var studentId = Long.parseLong(decodedJWT.getSubject());
        // Get tasks by state
        Set<Task> todoTasks = taskService.getTaskByState(studentId, TaskState.TODO);

        Set<Task> inProgressTasks = taskService.getTaskByState(studentId, TaskState.IN_PROGRESS);

        Set<Task> completedTasks = taskService.getTaskByState(studentId, TaskState.COMPLETE);

        // Get only recent tasks (last week)
        //Timestamp oneWeekAgo = Timestamp.from(Instant.now().minusSeconds(7 * 24 * 60 * 60));
        Set<Task> upcomingTasks = taskService.getTaskByStateAndIssueTime(studentId,
                TaskState.TODO,
                Timestamp.from(Instant.now().minus(7, ChronoUnit.DAYS)));

        Map<String, Object> response = new HashMap<>();
        response.put("todo", todoTasks);
        response.put("inProgress", inProgressTasks);
        response.put("completed", completedTasks);
        response.put("upcoming", upcomingTasks);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/tasks/{taskId}/state")
    public ResponseEntity<Task> updateTaskState(
            @PathVariable Long taskId,
            @RequestParam TaskState newState) {

        Task updatedTask = taskService.changeTaskState(taskId, newState);
        if (updatedTask == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedTask);
    }

    @PutMapping("/tasks/{taskId}")
    public ResponseEntity<Task> updateTask(
            @PathVariable Long taskId,
            @RequestBody Task task) {

        Task updatedTask = taskService.updateTask(taskId, task);
        if (updatedTask == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedTask);
    }
}