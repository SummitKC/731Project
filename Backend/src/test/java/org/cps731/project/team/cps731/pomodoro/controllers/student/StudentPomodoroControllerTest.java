package org.cps731.project.team.cps731.pomodoro.controllers.student;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.cps731.project.team.cps731.pomodoro.data.model.task.Task;
import org.cps731.project.team.cps731.pomodoro.data.model.task.TaskPriority;
import org.cps731.project.team.cps731.pomodoro.data.model.task.TaskState;
import org.cps731.project.team.cps731.pomodoro.data.model.timeentry.TimeEntry;
import org.cps731.project.team.cps731.pomodoro.data.model.user.Student;
import org.cps731.project.team.cps731.pomodoro.data.model.user.User;
import org.cps731.project.team.cps731.pomodoro.data.model.user.UserType;
import org.cps731.project.team.cps731.pomodoro.dto.PomSession;
import org.cps731.project.team.cps731.pomodoro.dto.task.TaskDTO;
import org.cps731.project.team.cps731.pomodoro.matchers.IsCloseToLong;
import org.cps731.project.team.cps731.pomodoro.security.filter.JwtAuthenticationFilter;
import org.cps731.project.team.cps731.pomodoro.security.principal.AppUserDetails;
import org.cps731.project.team.cps731.pomodoro.security.principal.authority.AppAuthorities;
import org.cps731.project.team.cps731.pomodoro.services.TaskService;
import org.cps731.project.team.cps731.pomodoro.services.TimeEntryService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class StudentPomodoroControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private TaskService taskService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private TimeEntryService timeEntryService;

    @SpyBean
    private StudentPomodoroController pomodoroController;


    @Test
    public void shouldReturnPomSessionForStartedPom() throws ServletException, IOException {
        var taskID = 1L;
        var mockUser = new User("john.something@torontomu.ca", "password", UserType.STUDENT);
        mockUser.setId(1L);
        var mockStudent = new Student(mockUser);
        mockStudent.setId(mockUser.getId());
        var mockTask = Task.builder()
                .name("Finish a1")
                .priority(TaskPriority.NORMAL)
                .plannedDueDate(Timestamp.from(Instant.now().plus(7, ChronoUnit.DAYS)))
                .state(TaskState.IN_PROGRESS)
                .owner(mockStudent)
                .build();
        mockTask.setID(taskID);
        var mockDecodedJWT = mock(DecodedJWT.class);
        when(mockDecodedJWT.getSubject()).thenReturn(mockStudent.getId().toString());
        doAnswer((Answer<Void>) invocation -> {
            var request = (ServletRequest) invocation.getArgument(0);
            var response = (ServletResponse) invocation.getArgument(1);
            var filterChain = (FilterChain) invocation.getArgument(2);
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                    new AppUserDetails(mockStudent.getUser()),
                    mockDecodedJWT,
                    Set.of(AppAuthorities.STUDENT)
            ));
            filterChain.doFilter(request, response);
            return null;
        }).when(jwtAuthenticationFilter).doFilter(any(ServletRequest.class), any(ServletResponse.class), any(FilterChain.class));
        when(taskService.getTaskById(taskID)).thenReturn(mockTask);

        var requestEntity = new HttpEntity<>(null, null);
        var response = restTemplate
                .exchange(
                        "http://localhost:" + port + "/api/student/pomodoro/start/" + taskID + "?mins=25",
                        HttpMethod.POST,
                        requestEntity,
                        PomSession.class
                );

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), notNullValue());
        assertThat(response.getBody(), hasProperty("resumeTime", equalTo(0L)));
        assertThat(response.getBody(), hasProperty("pauseTime", equalTo(0L)));
        assertThat(response.getBody(), hasProperty("task", equalTo(new TaskDTO(mockTask))));
        assertThat(response.getBody(), hasProperty("paused", equalTo(false)));
        assertThat(response.getBody(), hasProperty("pauses", equalTo(Collections.emptyList())));
    }

    @Test
    public void shouldReturnPomSessionWhenPausingPomAndBeUpdatedWithPauseTimeAndPaused() throws ServletException, IOException {
        var taskID = 1L;
        var mockUser = new User("john.something@torontomu.ca", "password", UserType.STUDENT);
        mockUser.setId(1L);
        var mockStudent = new Student(mockUser);
        mockStudent.setId(mockUser.getId());
        var mockTask = Task.builder()
                .name("Finish a1")
                .priority(TaskPriority.NORMAL)
                .plannedDueDate(Timestamp.from(Instant.now().plus(7, ChronoUnit.DAYS)))
                .state(TaskState.IN_PROGRESS)
                .owner(mockStudent)
                .build();
        mockTask.setID(taskID);
        var mockDecodedJWT = mock(DecodedJWT.class);
        when(mockDecodedJWT.getSubject()).thenReturn(mockStudent.getId().toString());
        when(taskService.getTaskById(taskID)).thenReturn(mockTask);
        var mockPomSession = PomSession.builder()
                .task(new TaskDTO(mockTask))
                .startTime(Instant.now().minus(10, ChronoUnit.MINUTES).toEpochMilli())
                .endTime(Instant.now().plus(15, ChronoUnit.MINUTES).toEpochMilli())
                .build();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                new AppUserDetails(mockStudent.getUser()),
                mockDecodedJWT,
                Set.of(AppAuthorities.STUDENT)
        ));
        doAnswer((Answer<Void>) invocation -> {
            var request = (ServletRequest) invocation.getArgument(0);
            var response = (ServletResponse) invocation.getArgument(1);
            var filterChain = (FilterChain) invocation.getArgument(2);
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                    new AppUserDetails(mockStudent.getUser()),
                    mockDecodedJWT,
                    Set.of(AppAuthorities.STUDENT)
            ));
            filterChain.doFilter(request, response);
            return null;
        }).when(jwtAuthenticationFilter).doFilter(any(ServletRequest.class), any(ServletResponse.class), any(FilterChain.class));
        var mockPom = PomSession.builder()
                .isPaused(false)
                .startTime(Instant.now().minus(15, ChronoUnit.MINUTES).toEpochMilli())
                .endTime(Instant.now().plus(10, ChronoUnit.MINUTES).toEpochMilli())
                .task(new TaskDTO(mockTask))
                .pauseTime(0L)
                .resumeTime(0L)
                .build();
        doReturn(Map.of(taskID, mockPom))
                .when(pomodoroController).getActivePomSessions();

        var requestEntity = new HttpEntity<>(null, null);
        var response = restTemplate
                .exchange(
                        "http://localhost:" + port + "/api/student/pomodoro/pause/" + taskID,
                        HttpMethod.POST,
                        requestEntity,
                        PomSession.class
                );

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), notNullValue());
        assertThat(response.getBody(), hasProperty("resumeTime", equalTo(0L)));
        System.out.println(Instant.now().toEpochMilli() - response.getBody().getPauseTime());
        assertThat(response.getBody(), hasProperty("pauseTime", IsCloseToLong.closeTo(Instant.now().toEpochMilli(), 100)));
        assertThat(response.getBody(), hasProperty("task", equalTo(mockPomSession.getTask())));
        assertThat(response.getBody(), hasProperty("paused", equalTo(true)));
        assertThat(response.getBody(), hasProperty("pauses", equalTo(Collections.emptyList())));
    }

    @Test
    public void shouldReturnPomSessionWhenResumingPomAndBeUpdatedWithResumeTimeAndPausedFalse() throws ServletException, IOException {
        var taskID = 1L;
        var mockUser = new User("john.something@torontomu.ca", "password", UserType.STUDENT);
        mockUser.setId(1L);
        var mockStudent = new Student(mockUser);
        mockStudent.setId(mockUser.getId());
        var mockTask = Task.builder()
                .name("Finish a1")
                .priority(TaskPriority.NORMAL)
                .plannedDueDate(Timestamp.from(Instant.now().plus(7, ChronoUnit.DAYS)))
                .state(TaskState.IN_PROGRESS)
                .owner(mockStudent)
                .build();
        mockTask.setID(taskID);
        var mockDecodedJWT = mock(DecodedJWT.class);
        when(mockDecodedJWT.getSubject()).thenReturn(mockStudent.getId().toString());
        when(taskService.getTaskById(taskID)).thenReturn(mockTask);
        var mockPomSession = PomSession.builder()
                .task(new TaskDTO(mockTask))
                .startTime(Instant.now().minus(10, ChronoUnit.MINUTES).toEpochMilli())
                .endTime(Instant.now().plus(15, ChronoUnit.MINUTES).toEpochMilli())
                .isPaused(true)
                .build();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                new AppUserDetails(mockStudent.getUser()),
                mockDecodedJWT,
                Set.of(AppAuthorities.STUDENT)
        ));
        doAnswer((Answer<Void>) invocation -> {
            var request = (ServletRequest) invocation.getArgument(0);
            var response = (ServletResponse) invocation.getArgument(1);
            var filterChain = (FilterChain) invocation.getArgument(2);
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                    new AppUserDetails(mockStudent.getUser()),
                    mockDecodedJWT,
                    Set.of(AppAuthorities.STUDENT)
            ));
            filterChain.doFilter(request, response);
            return null;
        }).when(jwtAuthenticationFilter).doFilter(any(ServletRequest.class), any(ServletResponse.class), any(FilterChain.class));
        var pauseTime = Instant.now().minus(5, ChronoUnit.MINUTES).toEpochMilli();
        var mockPom = PomSession.builder()
                .isPaused(true)
                .startTime(Instant.ofEpochMilli(pauseTime).minus(15, ChronoUnit.MINUTES).toEpochMilli())
                .endTime(Instant.ofEpochMilli(pauseTime).plus(10, ChronoUnit.MINUTES).toEpochMilli())
                .task(new TaskDTO(mockTask))
                .pauseTime(pauseTime)
                .resumeTime(0L)
                .build();
        doReturn(Map.of(taskID, mockPom))
                .when(pomodoroController).getActivePomSessions();

        var requestEntity = new HttpEntity<>(null, null);
        var response = restTemplate
                .exchange(
                        "http://localhost:" + port + "/api/student/pomodoro/resume/" + taskID,
                        HttpMethod.POST,
                        requestEntity,
                        PomSession.class
                );

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), notNullValue());
        assertThat(response.getBody(), hasProperty("pauseTime", equalTo(pauseTime)));
        assertThat(response.getBody(), hasProperty("resumeTime", IsCloseToLong.closeTo(Instant.now().toEpochMilli(), 100)));
        assertThat(response.getBody(), hasProperty("task", equalTo(mockPomSession.getTask())));
        assertThat(response.getBody(), hasProperty("paused", equalTo(false)));
        assertThat(response.getBody(), hasProperty("pauses", equalTo(List.of(response.getBody().getResumeTime() - pauseTime))));
        assertThat(response.getBody(), hasProperty("endTime",
                IsCloseToLong.closeTo(Instant.ofEpochMilli(pauseTime).plus(10, ChronoUnit.MINUTES).plusMillis(response.getBody().getPauses().getFirst()).toEpochMilli(), 100)));
    }

    @Test
    public void shouldReturnPomSessionWhenEndingPomAndSaveTimeLog() throws ServletException, IOException {
        var taskID = 1L;
        var mockUser = new User("john.something@torontomu.ca", "password", UserType.STUDENT);
        mockUser.setId(1L);
        var mockStudent = new Student(mockUser);
        mockStudent.setId(mockUser.getId());
        var mockTask = Task.builder()
                .name("Finish a1")
                .priority(TaskPriority.NORMAL)
                .plannedDueDate(Timestamp.from(Instant.now().plus(7, ChronoUnit.DAYS)))
                .state(TaskState.IN_PROGRESS)
                .owner(mockStudent)
                .build();
        mockTask.setID(taskID);
        var mockDecodedJWT = mock(DecodedJWT.class);
        when(mockDecodedJWT.getSubject()).thenReturn(mockStudent.getId().toString());
        when(taskService.getTaskById(taskID)).thenReturn(mockTask);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                new AppUserDetails(mockStudent.getUser()),
                mockDecodedJWT,
                Set.of(AppAuthorities.STUDENT)
        ));
        doAnswer((Answer<Void>) invocation -> {
            var request = (ServletRequest) invocation.getArgument(0);
            var response = (ServletResponse) invocation.getArgument(1);
            var filterChain = (FilterChain) invocation.getArgument(2);
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                    new AppUserDetails(mockStudent.getUser()),
                    mockDecodedJWT,
                    Set.of(AppAuthorities.STUDENT)
            ));
            filterChain.doFilter(request, response);
            return null;
        }).when(jwtAuthenticationFilter).doFilter(any(ServletRequest.class), any(ServletResponse.class), any(FilterChain.class));
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
        var mockPomSessions = new HashMap<>();
        mockPomSessions.put(taskID, mockPom);
        doReturn(mockPomSessions)
                .when(pomodoroController).getActivePomSessions();

        var requestEntity = new HttpEntity<>(null, null);
        var response = restTemplate
                .exchange(
                        "http://localhost:" + port + "/api/student/pomodoro/end/" + taskID,
                        HttpMethod.POST,
                        requestEntity,
                        PomSession.class
                );

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(mockPom));

        verify(timeEntryService).createTimeEntry(argThat(arg -> {
            assertThat(arg.getTask(), equalTo(mockTask));
            assertThat(arg.getStartTime().getTime(), equalTo(mockPom.getStartTime()));
            assertThat(arg.getEndTime().getTime(), equalTo(mockPom.getEndTime()));
            assertThat(arg.getTimeLogged(),
                    equalTo(mockPom.getEndTime() - mockPom.getStartTime() - mockPom.getPauses().stream().reduce((p1, p2) -> p1 + p1).orElseThrow()));
            return true;
        }));

    }

}
