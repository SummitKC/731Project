package org.cps731.project.team.cps731.pomodoro.data.model.assignment;

import jakarta.persistence.*;
import org.cps731.project.team.cps731.pomodoro.data.model.announcement.Announcement;
import org.cps731.project.team.cps731.pomodoro.data.model.task.Task;

import java.sql.Timestamp;
import java.util.Set;

@Entity
public class Assignment {

    @Id
    private Long ID;
    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "assignment_details_announcement", referencedColumnName = "ID")
    private Announcement announcement;
    private Timestamp dueDate;
    @OneToMany(mappedBy = "derivedFrom")
    private Set<Task> derivingTasks;

    public Assignment(Announcement announcement, Timestamp dueDate, Set<Task> derivingTasks) {
        this.announcement = announcement;
        this.dueDate = dueDate;
        this.derivingTasks = derivingTasks;
    }

    public Assignment(Announcement announcement, Timestamp dueDate) {
        this.announcement = announcement;
        this.dueDate = dueDate;
    }

    public Assignment() {
    }

    public static AssignmentBuilder builder() {
        return new AssignmentBuilder();
    }

    public Long getID() {
        return this.ID;
    }

    public Announcement getAnnouncement() {
        return this.announcement;
    }

    public Timestamp getDueDate() {
        return this.dueDate;
    }

    public Set<Task> getDerivingTasks() {
        return this.derivingTasks;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public void setAnnouncement(Announcement announcement) {
        this.announcement = announcement;
    }

    public void setDueDate(Timestamp dueDate) {
        this.dueDate = dueDate;
    }

    public void setDerivingTasks(Set<Task> derivingTasks) {
        this.derivingTasks = derivingTasks;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Assignment)) return false;
        final Assignment other = (Assignment) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$ID = this.getID();
        final Object other$ID = other.getID();
        if (this$ID == null ? other$ID != null : !this$ID.equals(other$ID)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Assignment;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $ID = this.getID();
        result = result * PRIME + ($ID == null ? 43 : $ID.hashCode());
        return result;
    }

    public static class AssignmentBuilder {
        private Announcement announcement;
        private Timestamp dueDate;
        private Set<Task> derivingTasks;

        AssignmentBuilder() {
        }

        public AssignmentBuilder announcement(Announcement announcement) {
            this.announcement = announcement;
            return this;
        }

        public AssignmentBuilder dueDate(Timestamp dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public AssignmentBuilder derivingTasks(Set<Task> derivingTasks) {
            this.derivingTasks = derivingTasks;
            return this;
        }

        public Assignment build() {
            return new Assignment(this.announcement, this.dueDate, this.derivingTasks);
        }

        public String toString() {
            return "Assignment.AssignmentBuilder(announcement=" + this.announcement + ", dueDate=" + this.dueDate + ", derivingTasks=" + this.derivingTasks + ")";
        }
    }
}
