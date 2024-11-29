package org.cps731.project.team.cps731.pomodoro.dto.analytics;

public class StudentAnalyticsDTO {

    private double averageSessionTime;
    private long maxSessionTime;
    private long minSessionTime;
    private long totalSessionTime;
    private int totalSessions;
    private int totalTasks;
    private int completedTasks;
    private double completionRate;
    private int taskCompletedThisMonth;


    public StudentAnalyticsDTO(double averageSessionTime, long maxSessionTime, long minSessionTime, long totalSessionTime, int totalSessions, int totalTasks, int completedTasks, double completionRate, int taskCompletedThisMonth) {
        this.averageSessionTime = averageSessionTime;
        this.maxSessionTime = maxSessionTime;
        this.minSessionTime = minSessionTime;
        this.totalSessionTime = totalSessionTime;
        this.totalSessions = totalSessions;
        this.totalTasks = totalTasks;
        this.completedTasks = completedTasks;
        this.completionRate = completionRate;
        this.taskCompletedThisMonth = taskCompletedThisMonth;
    }

    public StudentAnalyticsDTO() {
    }

    public static StudentAnalyticsDTOBuilder builder() {
        return new StudentAnalyticsDTOBuilder();
    }

    public double getAverageSessionTime() {
        return this.averageSessionTime;
    }

    public long getMaxSessionTime() {
        return this.maxSessionTime;
    }

    public long getMinSessionTime() {
        return this.minSessionTime;
    }

    public long getTotalSessionTime() {
        return this.totalSessionTime;
    }

    public int getTotalSessions() {
        return this.totalSessions;
    }

    public int getTotalTasks() {
        return this.totalTasks;
    }

    public int getCompletedTasks() {
        return this.completedTasks;
    }

    public double getCompletionRate() {
        return this.completionRate;
    }

    public int getTaskCompletedThisMonth() {
        return this.taskCompletedThisMonth;
    }

    public void setAverageSessionTime(double averageSessionTime) {
        this.averageSessionTime = averageSessionTime;
    }

    public void setMaxSessionTime(long maxSessionTime) {
        this.maxSessionTime = maxSessionTime;
    }

    public void setMinSessionTime(long minSessionTime) {
        this.minSessionTime = minSessionTime;
    }

    public void setTotalSessionTime(long totalSessionTime) {
        this.totalSessionTime = totalSessionTime;
    }

    public void setTotalSessions(int totalSessions) {
        this.totalSessions = totalSessions;
    }

    public void setTotalTasks(int totalTasks) {
        this.totalTasks = totalTasks;
    }

    public void setCompletedTasks(int completedTasks) {
        this.completedTasks = completedTasks;
    }

    public void setCompletionRate(double completionRate) {
        this.completionRate = completionRate;
    }

    public void setTaskCompletedThisMonth(int taskCompletedThisMonth) {
        this.taskCompletedThisMonth = taskCompletedThisMonth;
    }

    public static class StudentAnalyticsDTOBuilder {
        private double averageSessionTime;
        private long maxSessionTime;
        private long minSessionTime;
        private long totalSessionTime;
        private int totalSessions;
        private int totalTasks;
        private int completedTasks;
        private double completionRate;
        private int taskCompletedThisMonth;

        StudentAnalyticsDTOBuilder() {
        }

        public StudentAnalyticsDTOBuilder averageSessionTime(double averageSessionTime) {
            this.averageSessionTime = averageSessionTime;
            return this;
        }

        public StudentAnalyticsDTOBuilder maxSessionTime(long maxSessionTime) {
            this.maxSessionTime = maxSessionTime;
            return this;
        }

        public StudentAnalyticsDTOBuilder minSessionTime(long minSessionTime) {
            this.minSessionTime = minSessionTime;
            return this;
        }

        public StudentAnalyticsDTOBuilder totalSessionTime(long totalSessionTime) {
            this.totalSessionTime = totalSessionTime;
            return this;
        }

        public StudentAnalyticsDTOBuilder totalSessions(int totalSessions) {
            this.totalSessions = totalSessions;
            return this;
        }

        public StudentAnalyticsDTOBuilder totalTasks(int totalTasks) {
            this.totalTasks = totalTasks;
            return this;
        }

        public StudentAnalyticsDTOBuilder completedTasks(int completedTasks) {
            this.completedTasks = completedTasks;
            return this;
        }

        public StudentAnalyticsDTOBuilder completionRate(double completionRate) {
            this.completionRate = completionRate;
            return this;
        }

        public StudentAnalyticsDTOBuilder taskCompletedThisMonth(int taskCompletedThisMonth) {
            this.taskCompletedThisMonth = taskCompletedThisMonth;
            return this;
        }

        public StudentAnalyticsDTO build() {
            return new StudentAnalyticsDTO(this.averageSessionTime, this.maxSessionTime, this.minSessionTime, this.totalSessionTime, this.totalSessions, this.totalTasks, this.completedTasks, this.completionRate, this.taskCompletedThisMonth);
        }

        public String toString() {
            return "StudentAnalyticsDTO.StudentAnalyticsDTOBuilder(averageSessionTime=" + this.averageSessionTime + ", maxSessionTime=" + this.maxSessionTime + ", minSessionTime=" + this.minSessionTime + ", totalSessionTime=" + this.totalSessionTime + ", totalSessions=" + this.totalSessions + ", totalTasks=" + this.totalTasks + ", completedTasks=" + this.completedTasks + ", completionRate=" + this.completionRate + ", taskCompletedThisMonth=" + this.taskCompletedThisMonth + ")";
        }
    }
}
