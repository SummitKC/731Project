package org.cps731.project.team.cps731.pomodoro.services;

import org.cps731.project.team.cps731.pomodoro.data.model.announcement.Announcement;
import org.cps731.project.team.cps731.pomodoro.data.model.assignment.Assignment;
import org.cps731.project.team.cps731.pomodoro.data.model.course.Course;
import org.cps731.project.team.cps731.pomodoro.data.model.user.UserType;
import org.cps731.project.team.cps731.pomodoro.data.repo.announcement.AnnouncementRepo;
import org.cps731.project.team.cps731.pomodoro.data.repo.assignment.AssignmentRepo;
import org.cps731.project.team.cps731.pomodoro.data.repo.user.ProfessorRepo;
import org.cps731.project.team.cps731.pomodoro.data.repo.user.StudentRepo;
import org.cps731.project.team.cps731.pomodoro.data.repo.user.UserRepo;
import org.cps731.project.team.cps731.pomodoro.dto.assignment.CreateAssignmentRequestDTO;
import org.cps731.project.team.cps731.pomodoro.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
public class AssignmentService {

    private final AssignmentRepo assignmentRepo;
    private final StudentRepo studentRepo;
    private final UserRepo userRepo;
    private final ProfessorRepo professorRepo;
    private final AnnouncementRepo announcementRepo;

    @Autowired
    public AssignmentService(AssignmentRepo assignmentRepo, StudentRepo studentRepo, UserRepo userRepo, ProfessorRepo professorRepo, AnnouncementRepo announcementRepo) {
        this.assignmentRepo = assignmentRepo;
        this.studentRepo = studentRepo;
        this.userRepo = userRepo;
        this.professorRepo = professorRepo;
        this.announcementRepo = announcementRepo;
    }

    public List<Assignment> getAssignmentsByCourse(String courseCode, int page, int size) {
        var userID = SecurityUtil.getAuthenticatedUserID();
        var user = userRepo.findById(userID).orElseThrow();

        if (user.getUserType().equals(UserType.STUDENT)) {
            var student = studentRepo.findById(userID).orElseThrow();
            if (student.getCourses().stream().noneMatch(c -> c.getCourseCode().equals(courseCode))) {
                throw new IllegalArgumentException("Student is not enrolled in this course");
            }
        } else if (user.getUserType().equals(UserType.PROFESSOR)) {
            var professor = professorRepo.findById(userID).orElseThrow();
            if (professor.getCreatedCourses().stream().noneMatch(c -> c.getCourseCode().equals(courseCode))) {
                throw new IllegalArgumentException("Professor does not own in this course");
            }
        }

        PageRequest pageRequest = PageRequest.of(page, size,
                Sort.by(Sort.Direction.DESC, "dueDate"));

        return assignmentRepo.findAllByAnnouncement_Course_CourseCode(courseCode, pageRequest);
    }

    public Assignment getAssignmentById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Assignment ID cannot be null");
        }
        return assignmentRepo.findById(id).orElseThrow(() -> new RuntimeException("Assignment not found with id: " + id));
    }

    public Assignment createAssignment(CreateAssignmentRequestDTO request, Course course) {
        var assignment = new Assignment(new Announcement(
                request.getAssignmentTitle(),
                new Timestamp(Instant.now().toEpochMilli()),
                request.getAssignmentDescription(),
                course
        ), new Timestamp(request.getAssignmentDueDate()));
        return assignmentRepo.save(assignment);
    }

    public Assignment updateAssignment(Long id, CreateAssignmentRequestDTO request) {
        if (id == null || request == null) {
            throw new IllegalArgumentException("ID and assignment cannot be null");
        }
        var announcementChanged = false;
        var existingAssignment = getAssignmentById(id);
        var matchingAnnouncement = announcementRepo.findById(id).orElseThrow();
        var userID = SecurityUtil.getAuthenticatedUserID();
        if (!existingAssignment.getAnnouncement().getCourse().getCreatedBy().getUserID().equals(userID)) {
            throw new AuthorizationDeniedException(
                    "Could not access this assignment",
                    new AuthorizationDecision(false)
            );
        }
        if (request.getAssignmentTitle() != null) {
            existingAssignment.getAnnouncement().setTitle(request.getAssignmentTitle());
            matchingAnnouncement.setTitle(request.getAssignmentTitle());
            announcementChanged = true;
        }
        if (request.getAssignmentDescription() != null) {
            existingAssignment.getAnnouncement().setDescription(request.getAssignmentDescription());
            matchingAnnouncement.setDescription(request.getAssignmentDescription());
            announcementChanged = true;
        }
        if (request.getAssignmentDueDate() != null) {
            existingAssignment.setDueDate(Timestamp.from(Instant.ofEpochMilli(request.getAssignmentDueDate())));
        }

        if (announcementChanged) {
            announcementRepo.save(matchingAnnouncement);
        }

        return assignmentRepo.save(existingAssignment);
    }

    public void deleteAssignment(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        if (!assignmentRepo.existsById(id)) {
            throw new RuntimeException("Assignment not found with id: " + id);
        }
        assignmentRepo.deleteById(id);
    }
}