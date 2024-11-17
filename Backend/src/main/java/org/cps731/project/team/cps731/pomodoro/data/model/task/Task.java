package org.cps731.project.team.cps731.pomodoro.data.model.task;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import org.cps731.project.team.cps731.pomodoro.data.model.assignment.Assignment;
import org.cps731.project.team.cps731.pomodoro.data.model.converter.DurationConverter;
import org.cps731.project.team.cps731.pomodoro.data.model.user.Student;

import java.sql.Timestamp;
import java.time.Duration;

@Entity
@EqualsAndHashCode(exclude = {"name", "plannedDueDate", "state", "timeLogged", "pomodorosCompleted", "owner", "derivedFrom"})
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ID;
    private String name;
    private Timestamp plannedDueDate;
    @Enumerated(EnumType.ORDINAL)
    private TaskState state;
    @Convert(converter = DurationConverter.class)
    private Duration timeLogged;
    private Integer pomodorosCompleted;
    @ManyToOne
    private Student owner;
    @ManyToOne
    @JoinColumn(name = "deriving_assignment", referencedColumnName = "assignment_details_announcement")
    private Assignment derivedFrom;

    public Task(String name, Timestamp plannedDueDate, TaskState state, Duration timeLogged, Integer pomodorosCompleted, Student owner, Assignment derivedFrom) {
        this.name = name;
        this.plannedDueDate = plannedDueDate;
        this.state = state;
        this.timeLogged = timeLogged;
        this.pomodorosCompleted = pomodorosCompleted;
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

    public Duration getTimeLogged() {
        return this.timeLogged;
    }

    public Integer getPomodorosCompleted() {
        return this.pomodorosCompleted;
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

    public void setTimeLogged(Duration timeLogged) {
        this.timeLogged = timeLogged;
    }

    public void setPomodorosCompleted(Integer pomodorosCompleted) {
        this.pomodorosCompleted = pomodorosCompleted;
    }

    public void setOwner(Student owner) {
        this.owner = owner;
    }

    public void setDerivedFrom(Assignment derivedFrom) {
        this.derivedFrom = derivedFrom;
    }

    public static class TaskBuilder {
        private String name;
        private Timestamp plannedDueDate;
        private TaskState state;
        private Duration timeLogged;
        private Integer pomodorosCompleted;
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

        public TaskBuilder timeLogged(Duration timeLogged) {
            this.timeLogged = timeLogged;
            return this;
        }

        public TaskBuilder pomodorosCompleted(Integer pomodorosCompleted) {
            this.pomodorosCompleted = pomodorosCompleted;
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
            return new Task(this.name, this.plannedDueDate, this.state, this.timeLogged, this.pomodorosCompleted, this.owner, this.derivedFrom);
        }

        public String toString() {
            return "Task.TaskBuilder(name=" + this.name + ", plannedDueDate=" + this.plannedDueDate + ", state=" + this.state + ", timeLogged=" + this.timeLogged + ", pomodorosCompleted=" + this.pomodorosCompleted + ", owner=" + this.owner + ", derivedFrom=" + this.derivedFrom + ")";
        }
    }
}
