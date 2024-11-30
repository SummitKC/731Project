package org.cps731.project.team.cps731.pomodoro.controllers.student;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.cps731.project.team.cps731.pomodoro.config.WebTestConfig;
import org.cps731.project.team.cps731.pomodoro.data.model.course.Course;
import org.cps731.project.team.cps731.pomodoro.data.model.course.Term;
import org.cps731.project.team.cps731.pomodoro.data.model.task.Task;
import org.cps731.project.team.cps731.pomodoro.data.model.task.TaskPriority;
import org.cps731.project.team.cps731.pomodoro.data.model.task.TaskState;
import org.cps731.project.team.cps731.pomodoro.data.model.user.Student;
import org.cps731.project.team.cps731.pomodoro.dto.course.CourseDTO;
import org.cps731.project.team.cps731.pomodoro.dto.course.JoinCourseRequestDTO;
import org.cps731.project.team.cps731.pomodoro.dto.student.StudentDashboardDTO;
import org.cps731.project.team.cps731.pomodoro.dto.student.StudentProfileDTO;
import org.cps731.project.team.cps731.pomodoro.dto.task.TaskDTO;
import org.cps731.project.team.cps731.pomodoro.security.SecurityUtil;
import org.cps731.project.team.cps731.pomodoro.security.filter.JwtAuthenticationFilter;
import org.cps731.project.team.cps731.pomodoro.services.CourseService;
import org.cps731.project.team.cps731.pomodoro.services.StudentService;
import org.cps731.project.team.cps731.pomodoro.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = StudentHomeController.class)
@Import(WebTestConfig.class)
public class StudentHomeControllerTest {

    @MockBean
    private StudentService studentService;
    @MockBean
    private CourseService courseService;
    @MockBean
    private TaskService taskService;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private MockMvc mockMvc;
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final ObjectWriter WRITER = MAPPER.writer().withDefaultPrettyPrinter();

    @BeforeEach
    public void setupTest() throws ServletException, IOException {
        doAnswer((Answer<Void>) invocation -> {
            var request = (ServletRequest) invocation.getArgument(0);
            var response = (ServletResponse) invocation.getArgument(1);
            var filterChain = (FilterChain) invocation.getArgument(2);
            filterChain.doFilter(request, response);
            return null;
        }).when(jwtAuthenticationFilter).doFilter(any(ServletRequest.class), any(ServletResponse.class), any(FilterChain.class));
    }

    @Test
    public void shouldReturnStudentProfileDTOWhenGettingStudentProfile() throws Exception {
        try (var securityUtil = mockStatic(SecurityUtil.class)) {
            var userID = 1L;
            var mockProfile = new StudentProfileDTO("John Smith", "john.smith@torontomu.ca", 1L);
            securityUtil.when(SecurityUtil::getAuthenticatedUserID).thenReturn(userID);
            when(studentService.getStudentProfile(userID))
                    .thenReturn(mockProfile);

            mockMvc.perform(get("/student/home/profile"))
                    .andExpect(status().isOk())
                    .andExpect(result -> {
                        var body = MAPPER.readerFor(StudentProfileDTO.class).readValue(result.getResponse().getContentAsString());
                        assertThat(body, equalTo(mockProfile));
                    });
        }
    }

    @Test
    public void shouldReturnStudentDashBoardDTOWhenCallingDashBoardEndpoint() throws Exception {
        try (var securityUtil = mockStatic(SecurityUtil.class)) {
            var userID = 1L;
            var mockStudent = mock(Student.class);
            securityUtil.when(SecurityUtil::getAuthenticatedUserID).thenReturn(userID);
            when(mockStudent.getID())
                    .thenReturn(userID);
            var mockCourse = Course.builder()
                    .courseCode("CPS406")
                    .name("Intro to software eng")
                    .term(Term.FALL)
                    .year(2024)
                    .archived(false)
                    .build();
            var mockUpcomingTask = Task.builder()
                    .state(TaskState.TODO)
                    .plannedDueDate(Timestamp.from(Instant.now().plus(4, ChronoUnit.DAYS)))
                    .owner(mockStudent)
                    .name("Finish A1")
                    .priority(TaskPriority.NORMAL)
                    .build();
            var mockInProgressTask = Task.builder()
                    .state(TaskState.IN_PROGRESS)
                    .plannedDueDate(Timestamp.from(Instant.now().plus(2, ChronoUnit.DAYS)))
                    .owner(mockStudent)
                    .name("Finish A2")
                    .build();
            when(mockStudent.getCourses()).thenReturn(Set.of(
                    mockCourse
            ));
            when(studentService.getStudentById(mockStudent.getID()))
                    .thenReturn(mockStudent);
            when(taskService.getTaskByStateAndIssueTime(any(), any(), any()))
                    .thenReturn(Set.of(mockUpcomingTask));
            when(taskService.getAllTasksByState(userID, TaskState.IN_PROGRESS))
                    .thenReturn(Set.of(mockInProgressTask));

            mockMvc.perform(get("/student/home/dashboard"))
                    .andExpect(status().isOk())
                    .andExpect(result -> {
                        var body = (StudentDashboardDTO) MAPPER.readerFor(StudentDashboardDTO.class).readValue(result.getResponse().getContentAsString());
                        var expected = new StudentDashboardDTO(
                                Set.of(new CourseDTO(mockCourse)),
                                Set.of(new TaskDTO(mockUpcomingTask)),
                                Set.of(new TaskDTO(mockInProgressTask))
                        );
                        assertThat(body.getCourses(), equalTo(expected.getCourses()));
                        assertThat(body.getUpcomingTasks(), hasSize(1));
                        assertThat(body.getUpcomingTasks().stream().findFirst().orElseThrow().getTaskStatus(),
                                equalTo(expected.getUpcomingTasks().stream().findFirst().orElseThrow().getTaskStatus()));
                        assertThat(body.getUpcomingTasks().stream().findFirst().orElseThrow().getTaskName(),
                                equalTo(expected.getUpcomingTasks().stream().findFirst().orElseThrow().getTaskName()));
                        assertThat(body.getUpcomingTasks().stream().findFirst().orElseThrow().getTaskPriority(),
                                equalTo(expected.getUpcomingTasks().stream().findFirst().orElseThrow().getTaskPriority()));
                        assertThat(body.getUpcomingTasks().stream().findFirst().orElseThrow().getTaskDate().getTime(),
                                equalTo(expected.getUpcomingTasks().stream().findFirst().orElseThrow().getTaskDate().getTime()));

                    });
        }
    }

    @Test
    public void shouldReturnNoContentResponseWhenSuccessfullyJoiningACourse() throws Exception {
        try (var securityUtil = mockStatic(SecurityUtil.class)) {
            var userID = 1L;
            securityUtil.when(SecurityUtil::getAuthenticatedUserID).thenReturn(userID);
            var mockStudent = mock(Student.class);
            when(mockStudent.getID())
                    .thenReturn(userID);
            when(studentService.addCourseToStudent(any(), any()))
                    .thenReturn(mockStudent);

            mockMvc.perform(post("/student/home/courses/join")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(WRITER.writeValueAsString(new JoinCourseRequestDTO("CPS406"))))
                    .andExpect(status().isNoContent());
        }
    }

}
