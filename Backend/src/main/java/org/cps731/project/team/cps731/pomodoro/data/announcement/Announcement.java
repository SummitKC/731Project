package org.cps731.project.team.cps731.pomodoro.data.announcement;

import jakarta.persistence.*;
import org.cps731.project.team.cps731.pomodoro.data.course.Course;

@Entity
public class Announcement {

    @EmbeddedId
    private AnnouncementID id;
    private String description;
    @ManyToOne
    @MapsId("courseID")
    @JoinColumns({
            @JoinColumn(name="course_name", referencedColumnName = "course_name"),
            @JoinColumn(name="school_term", referencedColumnName = "school_term"),
            @JoinColumn(name="school_year", referencedColumnName = "school_year")
    })
    private Course course;

}
