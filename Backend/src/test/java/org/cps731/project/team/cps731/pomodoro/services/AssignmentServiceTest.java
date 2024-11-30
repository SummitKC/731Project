package org.cps731.project.team.cps731.pomodoro.services;

import org.cps731.project.team.cps731.pomodoro.data.model.announcement.Announcement;
import org.cps731.project.team.cps731.pomodoro.data.model.assignment.Assignment;
import org.cps731.project.team.cps731.pomodoro.data.model.course.Course;
import org.cps731.project.team.cps731.pomodoro.data.model.user.Professor;
import org.cps731.project.team.cps731.pomodoro.data.model.user.User;
import org.cps731.project.team.cps731.pomodoro.data.repo.announcement.AnnouncementRepo;
import org.cps731.project.team.cps731.pomodoro.data.repo.assignment.AssignmentRepo;
import org.cps731.project.team.cps731.pomodoro.data.repo.user.ProfessorRepo;
import org.cps731.project.team.cps731.pomodoro.data.repo.user.StudentRepo;
import org.cps731.project.team.cps731.pomodoro.data.repo.user.UserRepo;
import org.cps731.project.team.cps731.pomodoro.dto.assignment.CreateAssignmentRequestDTO;
import org.cps731.project.team.cps731.pomodoro.security.SecurityUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = {
        AssignmentService.class,
        AssignmentRepo.class,
        StudentRepo.class,
        UserRepo.class,
        ProfessorRepo.class
        })
public class AssignmentServiceTest {

    @MockBean
    private AssignmentRepo assignmentRepo;
    @MockBean
    private StudentRepo studentRepo;
    @MockBean
    private UserRepo userRepo;
    @MockBean
    private ProfessorRepo professorRepo;
    @MockBean
    private AnnouncementRepo announcementRepo;
    @Autowired
    private AssignmentService assignmentService;

    @Test
    public void shouldUpdateAssignmentWhenCallingUpdateAssignmentMethod() {
        try (var securityUtil = mockStatic(SecurityUtil.class)) {
            var userID = 1L;
            var mockUser = mock(User.class);
            when(mockUser.getId()).thenReturn(userID);
            var mockProfessor = mock(Professor.class);
            when(mockProfessor.getUserID()).thenReturn(userID);
            var mockCourse = mock(Course.class);
            var mockAnnouncement = mock(Announcement.class);
            var assignmentID = 1L;
            var mockAssignment = mock(Assignment.class);
            when(mockAssignment.getAnnouncement()).thenReturn(mockAnnouncement);
            when(mockAnnouncement.getCourse()).thenReturn(mockCourse);
            when(mockAssignment.getID())
                    .thenReturn(assignmentID);
            when(mockCourse.getCreatedBy()).thenReturn(mockProfessor);
            securityUtil.when(SecurityUtil::getAuthenticatedUserID).thenReturn(userID);
            when(assignmentRepo.findById(assignmentID)).thenReturn(Optional.of(mockAssignment));
            when(announcementRepo.findById(assignmentID)).thenReturn(Optional.of(mockAnnouncement));

            var mockRequest = new CreateAssignmentRequestDTO("Hello World", "Hello World", Instant.now().plus(7, ChronoUnit.DAYS).toEpochMilli());
            assignmentService.updateAssignment(assignmentID, mockRequest);

            verify(mockAnnouncement, atLeast(1))
                    .setTitle(mockRequest.getAssignmentTitle());
            verify(mockAnnouncement, atLeast(1))
                    .setDescription(mockRequest.getAssignmentDescription());
            verify(mockAssignment, atLeast(1))
                    .setDueDate(Timestamp.from(Instant.ofEpochMilli(mockRequest.getAssignmentDueDate())));
            verify(announcementRepo, atLeast(1))
                    .save(mockAnnouncement);
            verify(assignmentRepo, atLeast(1))
                    .save(mockAssignment);
        }
    }

}
