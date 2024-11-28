package org.cps731.project.team.cps731.pomodoro.data.model.timeentry;

import jakarta.persistence.*;
import org.cps731.project.team.cps731.pomodoro.data.model.task.Task;

import java.sql.Timestamp;

@Entity
public class TimeEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Task task;
    private Timestamp startTime;
    private Timestamp endTime;
    private Long timeLogged;
    private int pomodoros;

    public TimeEntry(Task task, Timestamp startTime, Timestamp endTime, Long timeLogged, int pomodoros) {
        this.task = task;
        this.startTime = startTime;
        this.endTime = endTime;
        this.timeLogged = timeLogged;
        this.pomodoros = pomodoros;
    }

    public TimeEntry() {
    }

    public static TimeEntryBuilder builder() {
        return new TimeEntryBuilder();
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Long getTimeLogged() {
        return timeLogged;
    }

    public void setTimeLogged(Long timeLogged) {
        this.timeLogged = timeLogged;
    }

    public int getPomodoros() {
        return pomodoros;
    }

    public void setPomodoros(int pomodoros) {
        this.pomodoros = pomodoros;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof TimeEntry)) return false;
        final TimeEntry other = (TimeEntry) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.id;
        final Object other$id = other.id;
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof TimeEntry;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.id;
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        return result;
    }

    public static class TimeEntryBuilder {
        private Task task;
        private Timestamp startTime;
        private Timestamp endTime;
        private Long timeLogged;
        private int pomodoros;

        TimeEntryBuilder() {
        }

        public TimeEntryBuilder task(Task task) {
            this.task = task;
            return this;
        }

        public TimeEntryBuilder startTime(Timestamp startTime) {
            this.startTime = startTime;
            return this;
        }

        public TimeEntryBuilder endTime(Timestamp endTime) {
            this.endTime = endTime;
            return this;
        }

        public TimeEntryBuilder timeLogged(Long timeLogged) {
            this.timeLogged = timeLogged;
            return this;
        }

        public TimeEntryBuilder pomodoros(int pomodoros) {
            this.pomodoros = pomodoros;
            return this;
        }

        public TimeEntry build() {
            return new TimeEntry(this.task, this.startTime, this.endTime, this.timeLogged, this.pomodoros);
        }

        public String toString() {
            return "TimeEntry.TimeEntryBuilder(task=" + this.task + ", startTime=" + this.startTime + ", endTime=" + this.endTime + ", timeLogged=" + this.timeLogged + ", pomodoros=" + this.pomodoros + ")";
        }
    }
}
