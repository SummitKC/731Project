package org.cps731.project.team.cps731.pomodoro.dto.analytics;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
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

}
