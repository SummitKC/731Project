package org.cps731.project.team.cps731.pomodoro.data.announcement;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cps731.project.team.cps731.pomodoro.data.course.CourseID;

import java.sql.Timestamp;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AnnouncementID {

    private String title;
    @Column(name="issue_time")
    private Timestamp issueTime;
    @Embedded
    private CourseID courseID;

}
