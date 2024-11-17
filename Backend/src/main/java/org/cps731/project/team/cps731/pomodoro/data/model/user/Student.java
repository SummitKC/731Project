package org.cps731.project.team.cps731.pomodoro.data.model.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cps731.project.team.cps731.pomodoro.data.model.analytics.WorkAnalytics;
import org.cps731.project.team.cps731.pomodoro.data.model.course.Course;
import org.cps731.project.team.cps731.pomodoro.data.model.task.Task;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Student {

    @Id
    private Long id;
    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "ID")
    private User user;
    @ManyToMany(mappedBy = "takenBy")
    private Set<Course> courses;
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private Set<Task> tasks;
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private Set<WorkAnalytics> workAnalytics;

}
