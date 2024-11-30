package org.cps731.project.team.cps731.pomodoro.dto.assignment;

import org.cps731.project.team.cps731.pomodoro.data.model.assignment.Assignment;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AssignmentDTO {

    private Long assignmentID;
    private String assignmentTitle;
    private String assignmentDueDate;
    private String assignmentDueTime;

    public AssignmentDTO(Assignment assignment) {
        assignmentID = assignment.getID();
        assignmentTitle = assignment.getAnnouncement().getTitle();
        assignmentDueDate = new SimpleDateFormat("MM/dd/yyyy").format(new Date(assignment.getDueDate().getTime()));
        assignmentDueTime = new SimpleDateFormat("hh:mm a").format(new Date(assignment.getDueDate().getTime()));
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

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof AssignmentDTO)) return false;
        final AssignmentDTO other = (AssignmentDTO) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$assignmentID = this.getAssignmentID();
        final Object other$assignmentID = other.getAssignmentID();
        if (this$assignmentID == null ? other$assignmentID != null : !this$assignmentID.equals(other$assignmentID))
            return false;
        final Object this$assignmentTitle = this.getAssignmentTitle();
        final Object other$assignmentTitle = other.getAssignmentTitle();
        if (this$assignmentTitle == null ? other$assignmentTitle != null : !this$assignmentTitle.equals(other$assignmentTitle))
            return false;
        final Object this$assignmentDueDate = this.getAssignmentDueDate();
        final Object other$assignmentDueDate = other.getAssignmentDueDate();
        if (this$assignmentDueDate == null ? other$assignmentDueDate != null : !this$assignmentDueDate.equals(other$assignmentDueDate))
            return false;
        final Object this$assignmentDueTime = this.getAssignmentDueTime();
        final Object other$assignmentDueTime = other.getAssignmentDueTime();
        if (this$assignmentDueTime == null ? other$assignmentDueTime != null : !this$assignmentDueTime.equals(other$assignmentDueTime))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof AssignmentDTO;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $assignmentID = this.getAssignmentID();
        result = result * PRIME + ($assignmentID == null ? 43 : $assignmentID.hashCode());
        final Object $assignmentTitle = this.getAssignmentTitle();
        result = result * PRIME + ($assignmentTitle == null ? 43 : $assignmentTitle.hashCode());
        final Object $assignmentDueDate = this.getAssignmentDueDate();
        result = result * PRIME + ($assignmentDueDate == null ? 43 : $assignmentDueDate.hashCode());
        final Object $assignmentDueTime = this.getAssignmentDueTime();
        result = result * PRIME + ($assignmentDueTime == null ? 43 : $assignmentDueTime.hashCode());
        return result;
    }

    public String toString() {
        return "AssignmentDTO(assignmentID=" + this.getAssignmentID() + ", assignmentTitle=" + this.getAssignmentTitle() + ", assignmentDueDate=" + this.getAssignmentDueDate() + ", assignmentDueTime=" + this.getAssignmentDueTime() + ")";
    }
}
