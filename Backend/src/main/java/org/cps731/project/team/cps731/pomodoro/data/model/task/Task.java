package org.cps731.project.team.cps731.pomodoro.data.model.task;

import jakarta.persistence.*;
import org.cps731.project.team.cps731.pomodoro.data.model.assignment.Assignment;
import org.cps731.project.team.cps731.pomodoro.data.model.user.Student;

import java.sql.Timestamp;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ID;
    private String name;
    private Timestamp plannedDueDate;
    @Enumerated(EnumType.ORDINAL)
    private TaskState state;
    @ManyToOne
    private Student owner;
    @ManyToOne
    @JoinColumn(name = "deriving_assignment", referencedColumnName = "assignment_details_announcement")
    private Assignment derivedFrom;

    public Task(String name, Timestamp plannedDueDate, TaskState state, Student owner, Assignment derivedFrom) {
        this.name = name;
        this.plannedDueDate = plannedDueDate;
        this.state = state;
        this.owner = owner;
        this.derivedFrom = derivedFrom;
    }

    public Task() {
    }

    public static TaskBuilder builder() {
        return new TaskBuilder();
    }

    public Long getID() {
        return this.ID;
    }

    public String getName() {
        return this.name;
    }

    public Timestamp getPlannedDueDate() {
        return this.plannedDueDate;
    }

    public TaskState getState() {
        return this.state;
    }

    public Student getOwner() {
        return this.owner;
    }

    public Assignment getDerivedFrom() {
        return this.derivedFrom;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlannedDueDate(Timestamp plannedDueDate) {
        this.plannedDueDate = plannedDueDate;
    }

    public void setState(TaskState state) {
        this.state = state;
    }

    public void setOwner(Student owner) {
        this.owner = owner;
    }

    public void setDerivedFrom(Assignment derivedFrom) {
        this.derivedFrom = derivedFrom;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Task)) return false;
        final Task other = (Task) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$ID = this.getID();
        final Object other$ID = other.getID();
        if (this$ID == null ? other$ID != null : !this$ID.equals(other$ID)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Task;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $ID = this.getID();
        result = result * PRIME + ($ID == null ? 43 : $ID.hashCode());
        return result;
    }

    public static class TaskBuilder {
        private String name;
        private Timestamp plannedDueDate;
        private TaskState state;
        private Student owner;
        private Assignment derivedFrom;

        TaskBuilder() {
        }

        public TaskBuilder name(String name) {
            this.name = name;
            return this;
        }

        public TaskBuilder plannedDueDate(Timestamp plannedDueDate) {
            this.plannedDueDate = plannedDueDate;
            return this;
        }

        public TaskBuilder state(TaskState state) {
            this.state = state;
            return this;
        }

        public TaskBuilder owner(Student owner) {
            this.owner = owner;
            return this;
        }

        public TaskBuilder derivedFrom(Assignment derivedFrom) {
            this.derivedFrom = derivedFrom;
            return this;
        }

        public Task build() {
            return new Task(this.name, this.plannedDueDate, this.state, this.owner, this.derivedFrom);
        }

        public String toString() {
            return "Task.TaskBuilder(name=" + this.name + ", plannedDueDate=" + this.plannedDueDate + ", state=" + this.state + ", timeLogged=" + ", owner=" + this.owner + ", derivedFrom=" + this.derivedFrom + ")";
        }
    }
}
