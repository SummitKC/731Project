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
    private int pomodoros;

    public TimeEntry(Task task, Timestamp startTime, Timestamp endTime, int pomodoros) {
        this.task = task;
        this.startTime = startTime;
        this.endTime = endTime;
        this.pomodoros = pomodoros;
    }

    public TimeEntry() {
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
}
