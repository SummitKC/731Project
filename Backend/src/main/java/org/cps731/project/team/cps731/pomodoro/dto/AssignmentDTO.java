package org.cps731.project.team.cps731.pomodoro.dto;

import org.cps731.project.team.cps731.pomodoro.data.model.assignment.Assignment;

import java.text.SimpleDateFormat;

public class AssignmentDTO {

    private Long assignmentID;
    private String assignmentTitle;
    private String assignmentDueDate;
    private String assignmentDueTime;

    public AssignmentDTO(Assignment assignment) {
        assignmentID = assignment.getID();
        assignmentTitle = assignment.getAnnouncement().getTitle();
        assignmentDueDate = new SimpleDateFormat("MM/dd/yyyy").format(assignment.getDueDate().toLocalDateTime());
        assignmentDueTime = new SimpleDateFormat("hh:mm a").format(assignment.getDueDate().toLocalDateTime());
    }

    public AssignmentDTO() {
    }

    public Long getAssignmentID() {
        return this.assignmentID;
    }

    public String getAssignmentTitle() {
        return this.assignmentTitle;
    }

    public String getAssignmentDueDate() {
        return this.assignmentDueDate;
    }

    public String getAssignmentDueTime() {
        return this.assignmentDueTime;
    }

    public void setAssignmentID(Long assignmentID) {
        this.assignmentID = assignmentID;
    }

    public void setAssignmentTitle(String assignmentTitle) {
        this.assignmentTitle = assignmentTitle;
    }

    public void setAssignmentDueDate(String assignmentDueDate) {
        this.assignmentDueDate = assignmentDueDate;
    }

    public void setAssignmentDueTime(String assignmentDueTime) {
        this.assignmentDueTime = assignmentDueTime;
    }
}
