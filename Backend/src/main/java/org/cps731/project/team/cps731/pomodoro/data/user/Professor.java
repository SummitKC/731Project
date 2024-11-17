package org.cps731.project.team.cps731.pomodoro.data.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cps731.project.team.cps731.pomodoro.data.course.Course;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Professor {

    @Id
    private Long id;
    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "ID")
    private User user;
    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL)
    private Set<Course> createdCourses;

}
