package org.cps731.project.team.cps731.pomodoro.data.model.analytics;

import jakarta.persistence.Embeddable;

import java.sql.Timestamp;

@Embeddable
public class WorkAnalyticsID {

    private Long studentID;
    private Timestamp startDate;
    private Timestamp endDate;

    public WorkAnalyticsID(Long studentID, Timestamp startDate, Timestamp endDate) {
        this.studentID = studentID;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public WorkAnalyticsID() {
    }

    public Long getStudentID() {
        return this.studentID;
    }

    public Timestamp getStartDate() {
        return this.startDate;
    }

    public Timestamp getEndDate() {
        return this.endDate;
    }

    public void setStudentID(Long studentID) {
        this.studentID = studentID;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof WorkAnalyticsID)) return false;
        final WorkAnalyticsID other = (WorkAnalyticsID) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$studentID = this.getStudentID();
        final Object other$studentID = other.getStudentID();
        if (this$studentID == null ? other$studentID != null : !this$studentID.equals(other$studentID)) return false;
        final Object this$startDate = this.getStartDate();
        final Object other$startDate = other.getStartDate();
        if (this$startDate == null ? other$startDate != null : !this$startDate.equals(other$startDate)) return false;
        final Object this$endDate = this.getEndDate();
        final Object other$endDate = other.getEndDate();
        if (this$endDate == null ? other$endDate != null : !this$endDate.equals(other$endDate)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof WorkAnalyticsID;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $studentID = this.getStudentID();
        result = result * PRIME + ($studentID == null ? 43 : $studentID.hashCode());
        final Object $startDate = this.getStartDate();
        result = result * PRIME + ($startDate == null ? 43 : $startDate.hashCode());
        final Object $endDate = this.getEndDate();
        result = result * PRIME + ($endDate == null ? 43 : $endDate.hashCode());
        return result;
    }
}
