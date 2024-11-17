package org.cps731.project.team.cps731.pomodoro.data.model.announcement;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cps731.project.team.cps731.pomodoro.data.model.course.Course;

import java.sql.Timestamp;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ID;
    private String title;
    @Column(name="issue_time")
    private Timestamp issueTime;
    private String description;
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name="course_name", referencedColumnName = "course_name"),
            @JoinColumn(name="school_term", referencedColumnName = "school_term"),
            @JoinColumn(name="school_year", referencedColumnName = "school_year")
    })
    private Course course;

}
