package org.cps731.project.team.cps731.pomodoro.data.services;

import org.cps731.project.team.cps731.pomodoro.data.model.assignment.Assignment;
import org.cps731.project.team.cps731.pomodoro.data.model.course.CourseID;
import org.cps731.project.team.cps731.pomodoro.data.repo.assignment.AssignmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepo assignmentRepo;

    public List<Assignment> getAssignmentsByCourse(CourseID courseID, int page, int size) {
        if (courseID == null) {
            throw new IllegalArgumentException("CourseID cannot be null");
        }
        
        PageRequest pageRequest = PageRequest.of(page, size, 
            Sort.by(Sort.Direction.DESC, "dueDate"));
            
        return assignmentRepo.findAllByAnnouncement_Course_CourseID(courseID, pageRequest);
    }

    public Assignment getAssignmentById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Assignment ID cannot be null");
        }
        return assignmentRepo.findById(id).orElseThrow(() -> new RuntimeException("Assignment not found with id: " + id));
    }

    public Assignment createAssignment(Assignment assignment) {
        if (assignment == null) {
            throw new IllegalArgumentException("Assignment cannot be null");
        }
        if (assignment.getAnnouncement() == null) {
            throw new IllegalArgumentException("Assignment must have an announcement");
        }
        if (assignment.getDueDate() == null) {
            throw new IllegalArgumentException("Assignment must have a due date");
        }
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