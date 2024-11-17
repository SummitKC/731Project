package org.cps731.project.team.cps731.pomodoro.data.analytics;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Embeddable
public class WorkAnalyticsID {

    private Long studentID;
    private Timestamp startDate;
    private Timestamp endDate;

}
