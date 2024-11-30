package org.cps731.project.team.cps731.pomodoro.controllers.student;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.cps731.project.team.cps731.pomodoro.config.WebTestConfig;
import org.cps731.project.team.cps731.pomodoro.data.model.announcement.Announcement;
import org.cps731.project.team.cps731.pomodoro.data.model.assignment.Assignment;
import org.cps731.project.team.cps731.pomodoro.data.model.course.Course;
import org.cps731.project.team.cps731.pomodoro.data.model.user.Professor;
import org.cps731.project.team.cps731.pomodoro.data.model.user.Student;
import org.cps731.project.team.cps731.pomodoro.data.model.user.User;
import org.cps731.project.team.cps731.pomodoro.data.model.user.UserType;
import org.cps731.project.team.cps731.pomodoro.dto.course.FullCourseInfoDTO;
import org.cps731.project.team.cps731.pomodoro.security.filter.JwtAuthenticationFilter;
import org.cps731.project.team.cps731.pomodoro.security.principal.authority.AppAuthorities;
import org.cps731.project.team.cps731.pomodoro.services.AnnouncementService;
import org.cps731.project.team.cps731.pomodoro.services.AssignmentService;
import org.cps731.project.team.cps731.pomodoro.services.CourseService;
import org.cps731.project.team.cps731.pomodoro.services.StudentService;
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
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = StudentCoursePageController.class)
@Import(WebTestConfig.class)
public class StudentCoursePageControllerTest {

    @MockBean
    private CourseService courseService;
    @MockBean
    private AnnouncementService announcementService;
    @MockBean
    private AssignmentService assignmentService;
    @MockBean
    private StudentService studentService;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private MockMvc mockMvc;
    private static User mockUser;
    private static User mockUser2;
    private static Student mockStudent;
    private static Professor mockProfessor;
    private static Course mockCourse;
    private static Assignment mockAssignment;
    private static Assignment mockAssignment2;
    private static Announcement announcementForA1;
    private static Announcement announcementForA2;
    private static Announcement mockAnnouncment;
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final ObjectWriter WRITER = MAPPER.writer().withDefaultPrettyPrinter();

    @BeforeAll
    public static void setupMocks() {
        mockUser = new User("John Smith", "john.something@torontomu.ca", "password", UserType.STUDENT);
        mockUser.setId(1L);
        mockStudent = new Student(mockUser, mockUser.getId());
        mockStudent.setID(mockUser.getId());

        mockUser2 = new User("Elli Smith", "elli.something@torontomu.ca", "password", UserType.PROFESSOR);
        mockUser2.setId(2L);
        mockProfessor = new Professor(mockUser2, mockUser2.getId());
        mockProfessor.setUserID(mockUser2.getId());

        mockCourse = Course.builder()
                .name("Intro to software eng")
                .archived(false)
                .courseCode("CPS406")
                .createdBy(mockProfessor)
                .build();

        announcementForA1 = Announcement.builder()
                .title("A1 is out")
                .issueTime(Timestamp.from(Instant.now().minus(4, ChronoUnit.DAYS)))
                .course(mockCourse)
                .description("This is an announcement for A1")
                .build();
        announcementForA1.setID(1L);
        mockAssignment = Assignment.builder()
                .dueDate(Timestamp.from(Instant.now().plus(7, ChronoUnit.DAYS)))
                .announcement(announcementForA1)
                .build();

        announcementForA2 = Announcement.builder()
                .title("A2 is out")
                .issueTime(Timestamp.from(Instant.now().minus(1, ChronoUnit.DAYS)))
                .course(mockCourse)
                .description("This is an announcement for ")
                .build();
        announcementForA2.setID(2L);
        mockAssignment2 = Assignment.builder()
                .dueDate(Timestamp.from(Instant.now().plus(10, ChronoUnit.DAYS)))
                .announcement(announcementForA2)
                .build();

        mockAnnouncment = Announcement.builder()
                .title("Take a shower")
                .description("You all stink")
                .course(mockCourse)
                .issueTime(Timestamp.from(Instant.now().minus(1, ChronoUnit.DAYS)))
                .build();
        mockAnnouncment.setID(3L);

        mockCourse.setAnnouncements(Set.of(announcementForA1, announcementForA2, mockAnnouncment));
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

        when(assignmentService.getAssignmentsByCourse(mockCourse.getCourseCode(), 0, 10))
                .thenReturn(List.of(mockAssignment, mockAssignment2));
    }

    @Test
    public void shouldReturnFullCourseDetailsDTOWhenGettingCourseDetails() throws Exception {
        when(courseService.getCourseById(mockCourse.getCourseCode()))
                .thenReturn(mockCourse);
        var assignments = assignmentService.getAssignmentsByCourse(mockCourse.getCourseCode(), 0, 10);

        mockMvc.perform(get("/student/course/" + mockCourse.getCourseCode()))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    var body = MAPPER.readerFor(FullCourseInfoDTO.class)
                                    .readValue(result.getResponse().getContentAsString());
                    assertThat(body, equalTo(new FullCourseInfoDTO(mockCourse, assignments)));
                });
    }

}
