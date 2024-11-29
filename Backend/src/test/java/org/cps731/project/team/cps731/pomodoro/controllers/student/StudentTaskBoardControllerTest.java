package org.cps731.project.team.cps731.pomodoro.controllers.student;

import org.cps731.project.team.cps731.pomodoro.data.model.announcement.Announcement;
import org.cps731.project.team.cps731.pomodoro.data.model.assignment.Assignment;
import org.cps731.project.team.cps731.pomodoro.data.model.task.Task;
import org.cps731.project.team.cps731.pomodoro.data.model.task.TaskPriority;
import org.cps731.project.team.cps731.pomodoro.data.model.task.TaskState;
import org.cps731.project.team.cps731.pomodoro.data.model.user.Student;
import org.cps731.project.team.cps731.pomodoro.data.model.user.User;
import org.cps731.project.team.cps731.pomodoro.data.model.user.UserType;
import org.cps731.project.team.cps731.pomodoro.dto.task.TaskDTO;
import org.cps731.project.team.cps731.pomodoro.security.auth.JwtUtil;
import org.cps731.project.team.cps731.pomodoro.services.TaskService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ContextConfiguration(classes = {
//        StudentTaskBoardController.class,
//        TaskService.class,
//        JwtUtil.class
//})
@WebMvcTest(controllers = StudentTaskBoardController.class)
@ActiveProfiles("test")
@Disabled
public class StudentTaskBoardControllerTest {

    @MockBean
    private TaskService taskService;
    @MockBean
    private JwtUtil jwtUtil;
    @Autowired
    private MockMvc mockMvc;
    private static Student mockStudent;
    private static Task mockTask;
    private static Assignment mockAssignment;

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

        //var response = restTemplate.getForObject("http://localhost:" + port + "/api/student/taskboard/tasks/" + mockTask.getID(), TaskDTO.class);
        mockMvc.perform(
                get("/api/student/taskboard/tasks/" + mockTask.getID())
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.taskName").exists());
    }

    private static Announcement mockAnnouncement() {
        return new Announcement(

        );
    }

}
