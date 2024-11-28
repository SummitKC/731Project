package org.cps731.project.team.cps731.pomodoro.dto;

import org.cps731.project.team.cps731.pomodoro.dto.task.TaskDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PomSession {
    private long startTime;
    private long endTime;
    private long pauseTime;
    private long resumeTime;
    private boolean paused;
    private TaskDTO task;
    private List<Long> pauses;

    public PomSession(long startTime, long endTime, long pauseTime, long resumeTime, boolean paused, TaskDTO task) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.pauseTime = pauseTime;
        this.resumeTime = resumeTime;
        this.paused = paused;
        this.task = task;
        pauses = new ArrayList<>();
    }

    public PomSession() {
    }

    public void addPause(Long pauseTime) {
        pauses.add(pauseTime);
    }

    public List<Long> getPauses() {
        return Collections.unmodifiableList(pauses);
    }

    public static PomSessionBuilder builder() {
        return new PomSessionBuilder();
    }

    public long getStartTime() {
        return this.startTime;
    }

    public long getEndTime() {
        return this.endTime;
    }

    public long getPauseTime() {
        return this.pauseTime;
    }

    public long getResumeTime() {
        return this.resumeTime;
    }

    public boolean isPaused() {
        return this.paused;
    }

    public TaskDTO getTask() {
        return this.task;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setPauseTime(long pauseTime) {
        this.pauseTime = pauseTime;
    }

    public void setResumeTime(long resumeTime) {
        this.resumeTime = resumeTime;
    }

    public void setPaused(boolean isPaused) {
        this.paused = isPaused;
    }

    public void setTask(TaskDTO task) {
        this.task = task;
    }

    public void setPauses(List<Long> pauses) {
        this.pauses = pauses;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof PomSession)) return false;
        final PomSession other = (PomSession) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.getStartTime() != other.getStartTime()) return false;
        if (this.getEndTime() != other.getEndTime()) return false;
        if (this.getPauseTime() != other.getPauseTime()) return false;
        if (this.getResumeTime() != other.getResumeTime()) return false;
        if (this.isPaused() != other.isPaused()) return false;
        final Object this$task = this.getTask();
        final Object other$task = other.getTask();
        if (this$task == null ? other$task != null : !this$task.equals(other$task)) return false;
        final Object this$pauses = this.getPauses();
        final Object other$pauses = other.getPauses();
        if (this$pauses == null ? other$pauses != null : !this$pauses.equals(other$pauses)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof PomSession;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final long $startTime = this.getStartTime();
        result = result * PRIME + (int) ($startTime >>> 32 ^ $startTime);
        final long $endTime = this.getEndTime();
        result = result * PRIME + (int) ($endTime >>> 32 ^ $endTime);
        final long $pauseTime = this.getPauseTime();
        result = result * PRIME + (int) ($pauseTime >>> 32 ^ $pauseTime);
        final long $resumeTime = this.getResumeTime();
        result = result * PRIME + (int) ($resumeTime >>> 32 ^ $resumeTime);
        result = result * PRIME + (this.isPaused() ? 79 : 97);
        final Object $task = this.getTask();
        result = result * PRIME + ($task == null ? 43 : $task.hashCode());
        final Object $pauses = this.getPauses();
        result = result * PRIME + ($pauses == null ? 43 : $pauses.hashCode());
        return result;
    }

    public String toString() {
        return "PomSession(startTime=" + this.getStartTime() + ", endTime=" + this.getEndTime() + ", pauseTime=" + this.getPauseTime() + ", resumeTime=" + this.getResumeTime() + ", isPaused=" + this.isPaused() + ", task=" + this.getTask() + ", pauses=" + this.getPauses() + ")";
    }

    public static class PomSessionBuilder {
        private long startTime;
        private long endTime;
        private long pauseTime;
        private long resumeTime;
        private boolean isPaused;
        private TaskDTO task;

        PomSessionBuilder() {
        }

        public PomSessionBuilder startTime(long startTime) {
            this.startTime = startTime;
            return this;
        }

        public PomSessionBuilder endTime(long endTime) {
            this.endTime = endTime;
            return this;
        }

        public PomSessionBuilder pauseTime(long pauseTime) {
            this.pauseTime = pauseTime;
            return this;
        }

        public PomSessionBuilder resumeTime(long resumeTime) {
            this.resumeTime = resumeTime;
            return this;
        }

        public PomSessionBuilder isPaused(boolean isPaused) {
            this.isPaused = isPaused;
            return this;
        }

        public PomSessionBuilder task(TaskDTO task) {
            this.task = task;
            return this;
        }

        public PomSession build() {
            return new PomSession(this.startTime, this.endTime, this.pauseTime, this.resumeTime, this.isPaused, this.task);
        }

        public String toString() {
            return "PomSession.PomSessionBuilder(startTime=" + this.startTime + ", endTime=" + this.endTime + ", pauseTime=" + this.pauseTime + ", resumeTime=" + this.resumeTime + ", isPaused=" + this.isPaused + ", task=" + this.task + ")";
        }
    }
}