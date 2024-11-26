package org.cps731.project.team.cps731.pomodoro.data.model.task;

import jakarta.persistence.*;
import org.cps731.project.team.cps731.pomodoro.data.model.assignment.Assignment;
import org.cps731.project.team.cps731.pomodoro.data.model.user.Student;
import org.cps731.project.team.cps731.pomodoro.dto.TaskDTO;

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
    @Enumerated(EnumType.ORDINAL)
    private TaskPriority priority;
    @ManyToOne
    private Student owner;
    @ManyToOne
    @JoinColumn(name = "deriving_assignment", referencedColumnName = "assignment_details_announcement")
    private Assignment derivedFrom;

    public Task() {
    }

    public Task(String name, Timestamp plannedDueDate, TaskState state, TaskPriority priority, Student owner, Assignment derivedFrom) {
        this.name = name;
        this.plannedDueDate = plannedDueDate;
        this.state = state;
        this.priority = priority;
        this.owner = owner;
        this.derivedFrom = derivedFrom;
    }

    public void updateFromTaskDTO(TaskDTO taskDTO) {
        name = taskDTO.getTaskName() == null ? name : taskDTO.getTaskName();
        plannedDueDate = taskDTO.getTaskDate() == null ? plannedDueDate : new Timestamp(taskDTO.getTaskDate().getTime());
        state = taskDTO.getTaskStatus() == null ? state : taskDTO.getTaskStatus();
        priority = taskDTO.getTaskPriority() == null ? priority : taskDTO.getTaskPriority();
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

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
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
        private TaskPriority priority;
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

        public TaskBuilder priority(TaskPriority priority) {
            this.priority = priority;
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
            return new Task(this.name, this.plannedDueDate, this.state, this.priority, this.owner, this.derivedFrom);
        }

        public String toString() {
            return "Task.TaskBuilder(name=" + this.name + ", plannedDueDate=" + this.plannedDueDate + ", state=" + this.state + ", priority=" + this.priority + ", owner=" + this.owner + ", derivedFrom=" + this.derivedFrom + ")";
        }
    }
}
