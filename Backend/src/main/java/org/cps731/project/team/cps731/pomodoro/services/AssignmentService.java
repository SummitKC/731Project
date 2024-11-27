package org.cps731.project.team.cps731.pomodoro.services;

import org.cps731.project.team.cps731.pomodoro.data.model.announcement.Announcement;
import org.cps731.project.team.cps731.pomodoro.data.model.assignment.Assignment;
import org.cps731.project.team.cps731.pomodoro.data.model.course.Course;
import org.cps731.project.team.cps731.pomodoro.data.model.user.UserType;
import org.cps731.project.team.cps731.pomodoro.data.repo.assignment.AssignmentRepo;
import org.cps731.project.team.cps731.pomodoro.data.repo.user.ProfessorRepo;
import org.cps731.project.team.cps731.pomodoro.data.repo.user.StudentRepo;
import org.cps731.project.team.cps731.pomodoro.data.repo.user.UserRepo;
import org.cps731.project.team.cps731.pomodoro.dto.AnnouncementDTO;
import org.cps731.project.team.cps731.pomodoro.dto.AssignmentDTO;
import org.cps731.project.team.cps731.pomodoro.dto.CreateAssignmentRequestDTO;
import org.cps731.project.team.cps731.pomodoro.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepo assignmentRepo;
    @Autowired
    private StudentRepo studentRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ProfessorRepo professorRepo;

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

    public Assignment updateAssignment(Long id, Assignment assignment) {
        if (id == null || assignment == null) {
            throw new IllegalArgumentException("ID and assignment cannot be null");
        }

        Assignment existingAssignment = getAssignmentById(id);
        
        existingAssignment.setAnnouncement(assignment.getAnnouncement());
        existingAssignment.setDueDate(assignment.getDueDate());
        existingAssignment.setDerivingTasks(assignment.getDerivingTasks());
        
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