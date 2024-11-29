package org.cps731.project.team.cps731.pomodoro.controllers.student;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.cps731.project.team.cps731.pomodoro.config.WebTestConfig;
import org.cps731.project.team.cps731.pomodoro.data.model.announcement.Announcement;
import org.cps731.project.team.cps731.pomodoro.data.model.assignment.Assignment;
import org.cps731.project.team.cps731.pomodoro.data.model.task.Task;
import org.cps731.project.team.cps731.pomodoro.data.model.task.TaskPriority;
import org.cps731.project.team.cps731.pomodoro.data.model.task.TaskState;
import org.cps731.project.team.cps731.pomodoro.data.model.user.Student;
import org.cps731.project.team.cps731.pomodoro.data.model.user.User;
import org.cps731.project.team.cps731.pomodoro.data.model.user.UserType;
import org.cps731.project.team.cps731.pomodoro.dto.task.TaskDTO;
import org.cps731.project.team.cps731.pomodoro.security.filter.JwtAuthenticationFilter;
import org.cps731.project.team.cps731.pomodoro.services.TaskService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = StudentTaskBoardController.class)
@Import(WebTestConfig.class)
@ActiveProfiles("test")
public class StudentTaskBoardControllerTest {

    @MockBean
    private TaskService taskService;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private MockMvc mockMvc;
    private static Student mockStudent;
    private static Task mockTask;
    private static Assignment mockAssignment;
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final ObjectWriter WRITER = MAPPER.writer().withDefaultPrettyPrinter();

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

    @BeforeAll
    public static void setup() {
        mockStudent = new Student(
                new User("John Smith", "john.smith@torontomu.ca", "password", UserType.STUDENT),
                1L
        );
        mockAssignment = new Assignment(
                mockAnnouncement(),
                Timestamp.from(Instant.now().plus(10, ChronoUnit.DAYS))
        );
        mockAssignment.setID(1L);
        mockTask = new Task(
                        "Finish A1",
                        Timestamp.from(Instant.now().plus(7, ChronoUnit.DAYS)),
                        TaskState.TODO,
                        TaskPriority.NORMAL,
                        mockStudent,
                        mockAssignment
                );
        mockTask.setID(1L);
        mockAssignment.setDerivingTasks(Set.of(mockTask));
    }

    @Test
    public void shouldReturnTaskDTOForTaskWhenGettingTaskByID() throws Exception {
        when(taskService.getTaskById(mockTask.getID()))
                .thenReturn(mockTask);

        mockMvc.perform(
                get("/student/taskboard/tasks/" + mockTask.getID())
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.taskName").value(mockTask.getName()))
                .andExpect(jsonPath("$.taskStatus").value(mockTask.getState().toString()))
                .andExpect(jsonPath("$.taskPriority").value(mockTask.getPriority().toString()));
    }

    @Test
    public void shouldReturnNotFoundWhenGettingTaskByNonexistentID() throws Exception {
        doThrow(NoSuchElementException.class)
                .when(taskService)
                .getTaskById(mockTask.getID());

        mockMvc.perform(
                        get("/student/taskboard/tasks/" + mockTask.getID())
                ).andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnCreatedResponseWithLocationHeaderWhenPostingTask() throws Exception {
        when(taskService.createTask(any(), any()))
                .thenReturn(mockTask);

        mockMvc.perform(
                        post("/student/taskboard/tasks?assignment=" + mockAssignment.getID())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(WRITER.writeValueAsString(new TaskDTO(mockTask)))
                )
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/student/taskboard/tasks/" + mockTask.getID()));
    }

    @Test
    public void shouldReturnNoContentResponseWhenDeletingTask() throws Exception {
        mockMvc.perform(
                        delete("/student/taskboard/tasks/" + mockAssignment.getID())
                )
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnNotFoundResponseWhenDeletingNonExistentTask() throws Exception {
        doThrow(NoSuchElementException.class)
                .when(taskService)
                .deleteTask(2L);

        mockMvc.perform(
                        delete("/student/taskboard/tasks/2")
                )
                .andExpect(status().isNotFound());
    }

    private static Announcement mockAnnouncement() {
        var mockAssignment = mock(Announcement.class);
        when(mockAssignment.getID()).thenReturn(1L);
        return mockAssignment;
    }

}
