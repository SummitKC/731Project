package org.cps731.project.team.cps731.pomodoro.data.model.analytics;

import jakarta.persistence.*;
import org.cps731.project.team.cps731.pomodoro.data.model.user.Student;

@Entity
public class WorkAnalytics {

    @EmbeddedId
    private WorkAnalyticsID id;
    @ManyToOne
    @MapsId("ID")
    @JoinColumn(name = "student", referencedColumnName = "ID")
    private Student student;
    private Long pomodorosCompleted;
    private Integer timeLogged;

    public WorkAnalytics(WorkAnalyticsID id, Student student, Long pomodorosCompleted, Integer timeLogged) {
        this.id = id;
        this.student = student;
        this.pomodorosCompleted = pomodorosCompleted;
        this.timeLogged = timeLogged;
    }

    public WorkAnalytics() {
    }

    public static WorkAnalyticsBuilder builder() {
        return new WorkAnalyticsBuilder();
    }

    public WorkAnalyticsID getId() {
        return this.id;
    }

    public Student getStudent() {
        return this.student;
    }

    public Long getPomodorosCompleted() {
        return this.pomodorosCompleted;
    }

    public Integer getTimeLogged() {
        return this.timeLogged;
    }

    public void setId(WorkAnalyticsID id) {
        this.id = id;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setPomodorosCompleted(Long pomodorosCompleted) {
        this.pomodorosCompleted = pomodorosCompleted;
    }

    public void setTimeLogged(Integer timeLogged) {
        this.timeLogged = timeLogged;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof WorkAnalytics)) return false;
        final WorkAnalytics other = (WorkAnalytics) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof WorkAnalytics;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        return result;
    }

    public static class WorkAnalyticsBuilder {
        private WorkAnalyticsID id;
        private Student student;
        private Long pomodorosCompleted;
        private Integer timeLogged;

        WorkAnalyticsBuilder() {
        }

        public WorkAnalyticsBuilder id(WorkAnalyticsID id) {
            this.id = id;
            return this;
        }

        public WorkAnalyticsBuilder student(Student student) {
            this.student = student;
            return this;
        }

        public WorkAnalyticsBuilder pomodorosCompleted(Long pomodorosCompleted) {
            this.pomodorosCompleted = pomodorosCompleted;
            return this;
        }

        public WorkAnalyticsBuilder timeLogged(Integer timeLogged) {
            this.timeLogged = timeLogged;
            return this;
        }

        public WorkAnalytics build() {
            return new WorkAnalytics(this.id, this.student, this.pomodorosCompleted, this.timeLogged);
        }

        public String toString() {
            return "WorkAnalytics.WorkAnalyticsBuilder(id=" + this.id + ", student=" + this.student + ", pomodorosCompleted=" + this.pomodorosCompleted + ", timeLogged=" + this.timeLogged + ")";
        }
    }
}
