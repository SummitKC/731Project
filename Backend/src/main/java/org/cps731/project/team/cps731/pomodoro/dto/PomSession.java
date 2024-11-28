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
    private boolean isPaused;
    private TaskDTO task;
    private List<Long> pauses;

    public PomSession(long startTime, long endTime, long pauseTime, long resumeTime, boolean isPaused, TaskDTO task) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.pauseTime = pauseTime;
        this.resumeTime = resumeTime;
        this.isPaused = isPaused;
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
        return this.isPaused;
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
        this.isPaused = isPaused;
    }

    public void setTask(TaskDTO task) {
        this.task = task;
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