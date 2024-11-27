package org.cps731.project.team.cps731.pomodoro.dto;

public class CreateAssignmentRequestDTO {

    private String assignmentTitle;
    private String assignmentDescription;
    private Long assignmentDueDate;

    public CreateAssignmentRequestDTO(String assignmentTitle, String assignmentDescription, Long assignmentDueDate) {
        this.assignmentTitle = assignmentTitle;
        this.assignmentDescription = assignmentDescription;
        this.assignmentDueDate = assignmentDueDate;
    }

    public CreateAssignmentRequestDTO() {
    }

    public String getAssignmentTitle() {
        return this.assignmentTitle;
    }

    public String getAssignmentDescription() {
        return this.assignmentDescription;
    }

    public Long getAssignmentDueDate() {
        return this.assignmentDueDate;
    }

    public void setAssignmentTitle(String assignmentTitle) {
        this.assignmentTitle = assignmentTitle;
    }

    public void setAssignmentDescription(String assignmentDescription) {
        this.assignmentDescription = assignmentDescription;
    }

    public void setAssignmentDueDate(Long assignmentDueDate) {
        this.assignmentDueDate = assignmentDueDate;
    }
}
