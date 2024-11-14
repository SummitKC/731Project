package org.cps731.project.team.cps731.pomodoro.data.assignment;

import jakarta.persistence.*;
import org.cps731.project.team.cps731.pomodoro.data.announcement.Announcement;
import org.cps731.project.team.cps731.pomodoro.data.announcement.AnnouncementID;

import java.sql.Timestamp;

@Entity
public class Assignment {

    @EmbeddedId
    private AnnouncementID id;
    @OneToOne
    @MapsId
    @JoinColumns({
            @JoinColumn(name="course_name", referencedColumnName = "course_name"),
            @JoinColumn(name="school_term", referencedColumnName = "school_term"),
            @JoinColumn(name="school_year", referencedColumnName = "school_year"),
            @JoinColumn(name="title", referencedColumnName = "title"),
            @JoinColumn(name="issue_time", referencedColumnName = "issue_time")
    })
    private Announcement announcement;
    private Timestamp dueDate;

}
