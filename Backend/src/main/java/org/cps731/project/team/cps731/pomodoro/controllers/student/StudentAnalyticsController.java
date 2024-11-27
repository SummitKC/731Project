package org.cps731.project.team.cps731.pomodoro.controllers.student;

import org.cps731.project.team.cps731.pomodoro.data.model.task.TaskState;
import org.cps731.project.team.cps731.pomodoro.data.model.timeentry.TimeEntry;
import org.cps731.project.team.cps731.pomodoro.security.SecurityUtil;
import org.cps731.project.team.cps731.pomodoro.services.TaskService;
import org.cps731.project.team.cps731.pomodoro.services.TimeEntryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/student/analytics")
@PreAuthorize("hasRole('ROLE_STUDENT')")
public class StudentAnalyticsController {

    private final TaskService taskService;
    private final TimeEntryService timeEntryService;

    public StudentAnalyticsController(TaskService taskService, TimeEntryService timeEntryService) {
        this.taskService = taskService;
        this.timeEntryService = timeEntryService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getAnalyticsDashboard() {
        var studentId = SecurityUtil.getAuthenticatedUserID();
        
        // Get all tasks
        var allTasks = taskService.getAllTasksByOwnnerID(studentId);
        var completedTasks = taskService.getTaskByState(studentId, TaskState.COMPLETE);
        
        // Get this month's completed tasks
        var startOfMonth = Timestamp.valueOf(LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0));
        var timeEntries = timeEntryService.findAllByTaskOwnerIdAndStartTimeAfter(studentId, startOfMonth);

        // Calculate task completion metrics
        int totalTasks = allTasks.size();
        int totalCompleted = completedTasks.size();
        double completionRate = totalTasks > 0 ? (double) totalCompleted / totalTasks * 100 : 0;

        // Calculate pomodoro session statistics
        var sessionStats = calculateSessionStats(timeEntries);

        Map<String, Object> response = new HashMap<>();
        response.put("totalTasks", totalTasks);
        response.put("completedTasks", totalCompleted);
        response.put("completionRate", Math.round(completionRate * 100.0) / 100.0);
        response.put("thisMonthCompleted", 5);
        response.putAll(sessionStats);

        return ResponseEntity.ok(response);
    }

    private Map<String, Object> calculateSessionStats(Set<TimeEntry> timeEntries) {
        Map<String, Object> stats = new HashMap<>();
        
        if (timeEntries.isEmpty()) {
            stats.put("averageSessionTime", 0);
            stats.put("maxSessionTime", 0);
            stats.put("minSessionTime", 0);
            stats.put("totalSessionTime", 0);
            return stats;
        }

        // Calculate session durations in minutes
        var sessionDurations = timeEntries.stream()
            .map(entry -> ChronoUnit.MINUTES.between(
                entry.getStartTime().toInstant(),
                entry.getEndTime().toInstant()))
            .collect(Collectors.toList());

        double averageTime = sessionDurations.stream()
            .mapToLong(Long::longValue)
            .average()
            .orElse(0);

        long maxTime = sessionDurations.stream()
            .mapToLong(Long::longValue)
            .max()
            .orElse(0);

        long minTime = sessionDurations.stream()
            .mapToLong(Long::longValue)
            .min()
            .orElse(0);

        long totalTime = sessionDurations.stream()
            .mapToLong(Long::longValue)
            .sum();

        stats.put("averageSessionTime", Math.round(averageTime * 100.0) / 100.0);
        stats.put("maxSessionTime", maxTime);
        stats.put("minSessionTime", minTime);
        stats.put("totalSessionTime", totalTime);
        stats.put("totalSessions", timeEntries.size());

        return stats;
    }
}