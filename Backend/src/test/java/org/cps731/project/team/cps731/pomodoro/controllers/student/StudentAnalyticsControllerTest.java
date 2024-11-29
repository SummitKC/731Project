package org.cps731.project.team.cps731.pomodoro.controllers.student;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.cps731.project.team.cps731.pomodoro.config.WebTestConfig;
import org.cps731.project.team.cps731.pomodoro.data.model.task.Task;
import org.cps731.project.team.cps731.pomodoro.data.model.task.TaskState;
import org.cps731.project.team.cps731.pomodoro.data.model.timeentry.TimeEntry;
import org.cps731.project.team.cps731.pomodoro.security.SecurityUtil;
import org.cps731.project.team.cps731.pomodoro.security.filter.JwtAuthenticationFilter;
import org.cps731.project.team.cps731.pomodoro.services.TaskService;
import org.cps731.project.team.cps731.pomodoro.services.TimeEntryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = StudentAnalyticsController.class)
@Import(WebTestConfig.class)
@ActiveProfiles("test")
public class StudentAnalyticsControllerTest {

    @MockBean
    private TaskService taskService;
    @MockBean
    private TimeEntryService timeEntryService;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setupTest() throws ServletException, IOException {
        doAnswer(invocation -> {
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken("John Smith", null, Set.of())
            );
            FilterChain filterChain = invocation.getArgument(2, FilterChain.class);
            filterChain.doFilter(invocation.getArgument(0), invocation.getArgument(1));
            return null;
        }).when(jwtAuthenticationFilter).doFilter(any(), any(), any());
    }

    @Test
    public void shouldGenerateWorkAnalyticsForStudent() throws Exception {
        try (var securityUtils = mockStatic(SecurityUtil.class)) {
            var userID = 1L;
            var mockTasks = mockTasks();
            var mockTask1 = mockTask1();
            var mockTimeEntries = mockTimeEntries();
            securityUtils.when(SecurityUtil::getAuthenticatedUserID).thenReturn(userID);
            when(taskService.getAllTasksByOwnerID(userID))
                    .thenReturn(mockTasks);
            when(taskService.getAllTasksByState(userID, TaskState.COMPLETE))
                    .thenReturn(Set.of(mockTask1));
            when(timeEntryService.findAllByTaskOwnerIdAndStartTimeAfter(any(), any()))
                    .thenReturn(mockTimeEntries);
            when(taskService.getTasksCompletedThisMonth(userID))
                    .thenReturn(Set.of(mockTask1));

            mockMvc.perform(get("/student/analytics/dashboard"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.averageSessionTime").value(50))
                    .andExpect(jsonPath("$.maxSessionTime").value(75))
                    .andExpect(jsonPath("$.minSessionTime").value(25))
                    .andExpect(jsonPath("$.totalSessionTime").value(100))
                    .andExpect(jsonPath("$.totalSessions").value(2))
                    .andExpect(jsonPath("$.totalTasks").value(2))
                    .andExpect(jsonPath("$.completedTasks").value(1))
                    .andExpect(jsonPath("$.completedTasks").value(1))
                    .andExpect(jsonPath("$.completionRate").value(50.0))
                    .andExpect(jsonPath("$.taskCompletedThisMonth").value(1));
        }


    }

    private Set<TimeEntry> mockTimeEntries() {
        var entry1 = mock(TimeEntry.class);
        when(entry1.getStartTime()).thenReturn(Timestamp.from(Instant.now().minus(7, ChronoUnit.DAYS)));
        when(entry1.getEndTime()).thenReturn(Timestamp.from(Instant.now().minus(7, ChronoUnit.DAYS).plus(25, ChronoUnit.MINUTES)));
        var entry2 = mock(TimeEntry.class);
        when(entry2.getStartTime()).thenReturn(Timestamp.from(Instant.now().minus(3, ChronoUnit.DAYS)));
        when(entry2.getEndTime()).thenReturn(Timestamp.from(Instant.now().minus(3, ChronoUnit.DAYS).plus(75, ChronoUnit.MINUTES)));

        return Set.of(entry1, entry2);
    };

    private Task mockTask1() {
        var mockTask = mock(Task.class);
        doReturn(TaskState.COMPLETE)
                .when(mockTask)
                .getState();
        return mockTask;
    }

    private Task mockTask2() {
        var mockTask = mock(Task.class);
        doReturn(TaskState.IN_PROGRESS)
                .when(mockTask)
                .getState();
        return mockTask;
    }

    private Set<Task> mockTasks() {
        return Set.of(
                mockTask1(),
                mockTask2()
        );
    }

}
