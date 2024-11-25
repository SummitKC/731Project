package org.cps731.project.team.cps731.pomodoro.data.controllers;

import org.cps731.project.team.cps731.pomodoro.data.model.task.Task;
import org.cps731.project.team.cps731.pomodoro.data.model.task.TaskState;
import org.cps731.project.team.cps731.pomodoro.data.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/student/taskboard")
public class StudentTaskBoardController {

    @Autowired
    private TaskService taskService;


    @GetMapping("/{studentId}/tasks")
    public ResponseEntity<Map<String, Object>> getTaskBoard(
            @PathVariable Long studentId,
            @PathVariable Timestamp issueTime,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // Get tasks by state
        Set<Task> todoTasks = taskService.getTaskByState(studentId, TaskState.TODO);
        
        Set<Task> inProgressTasks = taskService.getTaskByState(studentId, TaskState.TODO);
        
        Set<Task> completedTasks = taskService.getTaskByState(studentId, TaskState.TODO);

        // Get only recent tasks (last week)
        //Timestamp oneWeekAgo = Timestamp.from(Instant.now().minusSeconds(7 * 24 * 60 * 60));
        Set<Task> upcomingTasks = taskService.getTaskByStateAndIssueTime(studentId, TaskState.TODO, issueTime);

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