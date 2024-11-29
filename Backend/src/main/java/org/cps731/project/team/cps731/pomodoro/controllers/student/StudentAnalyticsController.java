package org.cps731.project.team.cps731.pomodoro.controllers.student;

import org.cps731.project.team.cps731.pomodoro.data.model.task.TaskState;
import org.cps731.project.team.cps731.pomodoro.data.model.timeentry.TimeEntry;
import org.cps731.project.team.cps731.pomodoro.dto.analytics.StudentAnalyticsDTO;
import org.cps731.project.team.cps731.pomodoro.security.SecurityUtil;
import org.cps731.project.team.cps731.pomodoro.services.TaskService;
import org.cps731.project.team.cps731.pomodoro.services.TimeEntryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
    public ResponseEntity<StudentAnalyticsDTO> getAnalyticsDashboard() {
        var studentId = SecurityUtil.getAuthenticatedUserID();
        
        // Get all tasks
        var allTasks = taskService.getAllTasksByOwnerID(studentId);
        var completedTasks = taskService.getAllTasksByState(studentId, TaskState.COMPLETE);
        
        // Get this month's completed tasks
        var startOfMonth = Timestamp.valueOf(LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0));
        var timeEntries = timeEntryService.findAllByTaskOwnerIdAndStartTimeAfter(studentId, startOfMonth);

        // Calculate task completion metrics
        int totalTasks = allTasks.size();
        int totalCompleted = completedTasks.size();
        double completionRate = totalTasks > 0 ? (double) totalCompleted / totalTasks * 100 : 0;

        // Calculate pomodoro session statistics
        var sessionStats = calculateSessionStats(timeEntries);

        sessionStats.setTotalTasks(totalTasks);
        sessionStats.setCompletedTasks(totalCompleted);
        sessionStats.setCompletionRate(Math.round(completionRate * 100.0) / 100.0);
        sessionStats.setTaskCompletedThisMonth(taskService.getTasksCompletedThisMonth(studentId).size());

        return ResponseEntity.ok(sessionStats);
    }

    private StudentAnalyticsDTO calculateSessionStats(Set<TimeEntry> timeEntries) {
        if (timeEntries.isEmpty()) {
            return StudentAnalyticsDTO.builder()
                    .averageSessionTime(0)
                    .maxSessionTime(0)
                    .minSessionTime(0)
                    .totalSessionTime(0)
                    .build();
        }

        // Calculate session durations in minutes
        var sessionDurations = timeEntries.stream()
            .map(entry -> ChronoUnit.MINUTES.between(
                entry.getStartTime().toInstant(),
                entry.getEndTime().toInstant()))
            .toList();

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

        return StudentAnalyticsDTO.builder()
                .averageSessionTime(Math.round(averageTime * 100.0) / 100.0)
                .maxSessionTime(maxTime)
                .minSessionTime(minTime)
                .totalSessionTime(totalTime)
                .totalSessions(timeEntries.size())
                .build();
    }
}