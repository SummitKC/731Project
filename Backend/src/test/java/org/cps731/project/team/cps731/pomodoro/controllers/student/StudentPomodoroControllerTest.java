package org.cps731.project.team.cps731.pomodoro.controllers.student;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.cps731.project.team.cps731.pomodoro.config.WebTestConfig;
import org.cps731.project.team.cps731.pomodoro.data.model.task.Task;
import org.cps731.project.team.cps731.pomodoro.data.model.task.TaskPriority;
import org.cps731.project.team.cps731.pomodoro.data.model.task.TaskState;
import org.cps731.project.team.cps731.pomodoro.data.model.user.Student;
import org.cps731.project.team.cps731.pomodoro.data.model.user.User;
import org.cps731.project.team.cps731.pomodoro.data.model.user.UserType;
import org.cps731.project.team.cps731.pomodoro.dto.PomSession;
import org.cps731.project.team.cps731.pomodoro.dto.task.TaskDTO;
import org.cps731.project.team.cps731.pomodoro.matchers.IsCloseToInt;
import org.cps731.project.team.cps731.pomodoro.matchers.IsCloseToLong;
import org.cps731.project.team.cps731.pomodoro.security.filter.JwtAuthenticationFilter;
import org.cps731.project.team.cps731.pomodoro.security.principal.authority.AppAuthorities;
import org.cps731.project.team.cps731.pomodoro.services.PomodoroService;
import org.cps731.project.team.cps731.pomodoro.services.StudentService;
import org.cps731.project.team.cps731.pomodoro.services.TaskService;
import org.cps731.project.team.cps731.pomodoro.services.TimeEntryService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
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
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StudentPomodoroController.class)
@Import(WebTestConfig.class)
@ActiveProfiles("test")
public class StudentPomodoroControllerTest {

    public static final int TIME_MARGIN = 500;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PomodoroService pomodoroService;
    @MockBean
    private TaskService taskService;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean
    private TimeEntryService timeEntryService;
    @MockBean
    private StudentService studentService;
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final ObjectWriter WRITER = MAPPER.writer();
    private static Task mockTask;
    private static Task mockTask2;
    private static User mockUser;
    private static User mockUser2;
    private static Student mockStudent;
    private static Student mockStudent2;

    @BeforeAll
    public static void setupMocks() {
        mockUser = new User("John Smith", "john.something@torontomu.ca", "password", UserType.STUDENT);
        mockUser.setId(1L);
        mockStudent = new Student(mockUser, mockUser.getId());
        mockStudent.setID(mockUser.getId());
        mockUser2 = new User("Syed Smith", "syed.smith@torontomu.ca", "password", UserType.STUDENT);
        mockUser2.setId(2L);
        mockStudent2 = new Student(mockUser2, mockUser2.getId());
        mockStudent2.setID(mockUser2.getId());
        mockTask = Task.builder()
                .name("Finish a1")
                .priority(TaskPriority.NORMAL)
                .plannedDueDate(Timestamp.from(Instant.now().plus(7, ChronoUnit.DAYS)))
                .state(TaskState.IN_PROGRESS)
                .owner(mockStudent)
                .build();
        mockTask.setID(1L);
        mockTask2 = Task.builder()
                .name("Finish a2")
                .priority(TaskPriority.NORMAL)
                .plannedDueDate(Timestamp.from(Instant.now().plus(7, ChronoUnit.DAYS)))
                .state(TaskState.REVIEWING)
                .owner(mockStudent2)
                .build();
        mockTask2.setID(2L);
    }

    @BeforeEach
    public void setupTest() throws ServletException, IOException {
        doAnswer((Answer<Void>) invocation -> {
            var request = (ServletRequest) invocation.getArgument(0);
            var response = (ServletResponse) invocation.getArgument(1);
            var filterChain = (FilterChain) invocation.getArgument(2);
            var mockDecodedJWT = mock(DecodedJWT.class);
            when(mockDecodedJWT.getSubject()).thenReturn(mockStudent.getID().toString());
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken("John Smith", mockDecodedJWT, Set.of(AppAuthorities.STUDENT)))
            ;
            filterChain.doFilter(request, response);
            return null;
        }).when(jwtAuthenticationFilter).doFilter(any(ServletRequest.class), any(ServletResponse.class), any(FilterChain.class));
    }

    @Test
    public void shouldReturnPomSessionForStartedPom() throws Exception {
        when(studentService.getStudentById(mockStudent.getID()))
                .thenReturn(mockStudent);
        when(taskService.getTaskById(mockTask.getID())).thenReturn(mockTask);

        mockMvc.perform(post("/student/pomodoro/start/" + mockTask.getID() + "?mins=25"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startTime").exists())
                .andExpect(jsonPath("$.endTime").exists())
                .andExpect(jsonPath("$.resumeTime").value(0))
                .andExpect(jsonPath("$.pauseTime").value(0))
                .andExpect(jsonPath("$.paused").value(false));
    }

    @Test
    public void shouldReturnPomSessionWhenPausingPomAndBeUpdatedWithPauseTimeAndPaused() throws Exception {
        var mockPom = PomSession.builder()
                .isPaused(false)
                .startTime(Instant.now().minus(15, ChronoUnit.MINUTES).toEpochMilli())
                .endTime(Instant.now().plus(10, ChronoUnit.MINUTES).toEpochMilli())
                .task(new TaskDTO(mockTask))
                .pauseTime(0L)
                .resumeTime(0L)
                .build();
        when(studentService.getStudentById(mockStudent.getID()))
                .thenReturn(mockStudent);
        when(taskService.getTaskById(mockTask.getID())).thenReturn(mockTask);
        doReturn(mockPom)
                .when(pomodoroService)
                .getSession(mockTask.getID());

        mockMvc.perform(post("/student/pomodoro/pause/" + mockTask.getID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startTime").exists())
                .andExpect(jsonPath("$.endTime").exists())
                .andExpect(jsonPath("$.resumeTime").value(0))
                .andExpect(jsonPath("$.pauseTime").value(IsCloseToLong.closeTo(Instant.now().toEpochMilli(), TIME_MARGIN)))
                .andExpect(jsonPath("$.paused").value(true))
                .andExpect(jsonPath("$.pauses").value(equalTo(Collections.emptyList())));
    }

    @Test
    public void shouldReturnPomSessionWhenResumingPomAndBeUpdatedWithResumeTimeAndPausedFalse() throws Exception {
        var pauseTime = Instant.now().minus(5, ChronoUnit.MINUTES).toEpochMilli();
        var mockPom = PomSession.builder()
                .isPaused(true)
                .startTime(Instant.ofEpochMilli(pauseTime).minus(15, ChronoUnit.MINUTES).toEpochMilli())
                .endTime(Instant.ofEpochMilli(pauseTime).plus(10, ChronoUnit.MINUTES).toEpochMilli())
                .task(new TaskDTO(mockTask))
                .pauseTime(pauseTime)
                .resumeTime(0L)
                .build();
        when(studentService.getStudentById(mockStudent.getID()))
                .thenReturn(mockStudent);
        when(taskService.getTaskById(mockTask.getID())).thenReturn(mockTask);
        doReturn(mockPom)
                .when(pomodoroService)
                .getSession(mockTask.getID());

        mockMvc.perform(post("/student/pomodoro/resume/" + mockTask.getID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startTime").exists())
                .andExpect(jsonPath("$.endTime").exists())
                .andExpect(jsonPath("$.task.id").value(mockTask.getID()))
                .andExpect(jsonPath("$.task.taskName").value(mockTask.getName()))
                .andExpect(jsonPath("$.task.taskStatus").value(mockTask.getState().toString()))
                .andExpect(jsonPath("$.task.taskPriority").value(mockTask.getPriority().toString()))
                .andExpect(jsonPath("$.task.taskDate").exists())
                .andExpect(jsonPath("$.paused").value(false))
                .andExpect(jsonPath("$.pauses").value(hasSize(1)))
                .andExpect(jsonPath("$.pauses[0]").value(IsCloseToInt.closeTo((int) Duration.of(5, ChronoUnit.MINUTES).toMillis(), TIME_MARGIN)))
                .andExpect(jsonPath("$.endTime")
                        .value(IsCloseToLong.closeTo(Instant.ofEpochMilli(pauseTime).plus(15, ChronoUnit.MINUTES).toEpochMilli(), TIME_MARGIN)));
    }

    @Test
    public void shouldReturnPomSessionWhenEndingPomAndSaveTimeLog() throws Exception {
        when(taskService.getTaskById(mockTask.getID())).thenReturn(mockTask);
        when(studentService.getStudentById(mockStudent.getID()))
                .thenReturn(mockStudent);
        var pauseTime = Instant.now().minus(5, ChronoUnit.MINUTES).toEpochMilli();
        var mockPom = PomSession.builder()
                .isPaused(false)
                .startTime(Instant.ofEpochMilli(pauseTime).minus(15, ChronoUnit.MINUTES).toEpochMilli())
                .endTime(Instant.ofEpochMilli(pauseTime).plus(10, ChronoUnit.MINUTES).toEpochMilli())
                .task(new TaskDTO(mockTask))
                .pauseTime(pauseTime)
                .resumeTime(Instant.ofEpochMilli(pauseTime).plus(3, ChronoUnit.MINUTES).toEpochMilli())
                .build();
        mockPom.setPauses(List.of(mockPom.getResumeTime() - mockPom.getPauseTime()));
        doReturn(mockPom)
                .when(pomodoroService)
                .getSession(mockTask.getID());

        mockMvc.perform(post("/student/pomodoro/end/" + mockTask.getID()))
                .andExpect(jsonPath("$.startTime").value(mockPom.getStartTime()))
                .andExpect(jsonPath("$.endTime").value(mockPom.getEndTime()))
                .andExpect(jsonPath("$.task.id").value(mockTask.getID()))
                .andExpect(jsonPath("$.task.taskName").value(mockTask.getName()))
                .andExpect(jsonPath("$.task.taskStatus").value(mockTask.getState().toString()))
                .andExpect(jsonPath("$.task.taskPriority").value(mockTask.getPriority().toString()))
                .andExpect(jsonPath("$.task.taskDate").exists())
                .andExpect(jsonPath("$.paused").value(false))
                .andExpect(jsonPath("$.pauseTime").value(mockPom.getPauseTime()))
                .andExpect(jsonPath("$.resumeTime").value(mockPom.getResumeTime()))
                .andExpect(jsonPath("$.pauses").value(hasSize(1)))
                .andExpect(jsonPath("$.pauses[0]").value(mockPom.getPauses().getFirst()));
        verify(timeEntryService).createTimeEntry(argThat(arg -> {
            assertThat(arg.getTask(), equalTo(mockTask));
            assertThat(arg.getStartTime().getTime(), equalTo(mockPom.getStartTime()));
            assertThat(arg.getEndTime().getTime(), equalTo(mockPom.getEndTime()));
            assertThat(arg.getTimeLogged(),
                    equalTo(mockPom.getEndTime() - mockPom.getStartTime() - mockPom.getPauses().stream().reduce((p1, p2) -> p1 + p1).orElseThrow()));
            return true;
        }));

    }

    @Test
    public void shouldDenyStudentFromStartingSessionForUnownedTask() throws Exception {
        when(taskService.getTaskById(mockTask2.getID())).thenReturn(mockTask2);
        when(studentService.getStudentById(mockStudent.getID())).thenReturn(mockStudent);

        mockMvc.perform(post("/student/pomodoro/start/" + mockTask2.getID() + "?mins=25"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldDenyStudentFromPausingSessionForUnownedTask() throws Exception {
        when(taskService.getTaskById(mockTask2.getID())).thenReturn(mockTask2);
        when(studentService.getStudentById(mockStudent.getID())).thenReturn(mockStudent);

        mockMvc.perform(post("/student/pomodoro/pause/" + mockTask2.getID()))
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldDenyStudentFromResumingSessionForUnownedTask() throws Exception {
        when(taskService.getTaskById(mockTask2.getID())).thenReturn(mockTask2);
        when(studentService.getStudentById(mockStudent.getID())).thenReturn(mockStudent);

        mockMvc.perform(post("/student/pomodoro/resume/" + mockTask2.getID()))
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldDenyStudentFromEndingSessionForUnownedTask() throws Exception {
        when(taskService.getTaskById(mockTask2.getID())).thenReturn(mockTask2);
        when(studentService.getStudentById(mockStudent.getID())).thenReturn(mockStudent);

        mockMvc.perform(post("/student/pomodoro/end/" + mockTask2.getID()))
                .andExpect(status().isForbidden());
    }

}
