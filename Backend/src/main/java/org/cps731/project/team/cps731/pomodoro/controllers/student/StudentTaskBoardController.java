package org.cps731.project.team.cps731.pomodoro.controllers.student;

import org.cps731.project.team.cps731.pomodoro.data.model.task.Task;
import org.cps731.project.team.cps731.pomodoro.data.model.task.TaskState;
import org.cps731.project.team.cps731.pomodoro.dto.TaskDTO;
import org.cps731.project.team.cps731.pomodoro.security.SecurityUtil;
import org.cps731.project.team.cps731.pomodoro.security.auth.JwtUtil;
import org.cps731.project.team.cps731.pomodoro.services.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/student/taskboard")
@Secured("STUDENT")
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

        var studentId = SecurityUtil.getAuthenticatedUserID();
        // Get tasks by state
        Set<TaskDTO> todoTasks = taskService.getTaskByState(studentId, TaskState.TODO).stream().map(TaskDTO::new).collect(Collectors.toSet());

        Set<TaskDTO> inProgressTasks = taskService.getTaskByState(studentId, TaskState.IN_PROGRESS).stream().map(TaskDTO::new).collect(Collectors.toSet());

        Set<TaskDTO> completedTasks = taskService.getTaskByState(studentId, TaskState.COMPLETE).stream().map(TaskDTO::new).collect(Collectors.toSet());

        // Get only recent tasks (last week)
        //Timestamp oneWeekAgo = Timestamp.from(Instant.now().minusSeconds(7 * 24 * 60 * 60));
        Set<TaskDTO> upcomingTasks = taskService.getTaskByStateAndIssueTime(studentId,
                TaskState.TODO,
                Timestamp.from(Instant.now().minus(7, ChronoUnit.DAYS)))
                .stream().map(TaskDTO::new).collect(Collectors.toSet());

        Map<String, Object> response = new HashMap<>();
        response.put("todo", todoTasks);
        response.put("inProgress", inProgressTasks);
        response.put("completed", completedTasks);
        response.put("upcoming", upcomingTasks);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/tasks/{taskId}/state")
    public ResponseEntity<TaskDTO> updateTaskState(
            @PathVariable Long taskId,
            @RequestParam TaskState newState) {

        Task updatedTask = taskService.changeTaskState(taskId, newState);
        if (updatedTask == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new TaskDTO(updatedTask));
    }

    @PutMapping("/tasks/{taskId}")
    public ResponseEntity<TaskDTO> updateTask(
            @PathVariable Long taskId,
            @RequestBody TaskDTO task) {

        Task updatedTask = taskService.updateTask(taskId, task);
        if (updatedTask == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new TaskDTO(updatedTask));
    }
}