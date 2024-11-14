package org.cps731.project.team.cps731.pomodoro.data.course;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CourseID {

    @Column(name = "course_name")
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(name = "school_term")
    private Term term;
    @Column(name = "school_year")
    private Integer year;

}
