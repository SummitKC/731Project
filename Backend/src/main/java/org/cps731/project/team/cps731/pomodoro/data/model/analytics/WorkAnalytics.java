package org.cps731.project.team.cps731.pomodoro.data.model.analytics;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cps731.project.team.cps731.pomodoro.data.model.user.Student;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class WorkAnalytics {

    @EmbeddedId
    private WorkAnalyticsID id;
    @ManyToOne
    @MapsId("studentID")
    @JoinColumn(name = "student", referencedColumnName = "ID")
    private Student student;
    private Long pomodorosCompleted;
    private Integer timeLogged;

}
