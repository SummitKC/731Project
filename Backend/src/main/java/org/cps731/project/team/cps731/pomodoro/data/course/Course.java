package org.cps731.project.team.cps731.pomodoro.data.course;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cps731.project.team.cps731.pomodoro.data.announcement.Announcement;
import org.cps731.project.team.cps731.pomodoro.data.user.Professor;
import org.cps731.project.team.cps731.pomodoro.data.user.Student;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Course {

    @EmbeddedId
    private CourseID courseID;
    private Boolean archived;
    @ManyToOne
    private Professor createdBy;
    @ManyToMany
    @JoinTable(
            name = "StudentCourse",
            joinColumns = {
                    @JoinColumn(name="course_name", referencedColumnName = "course_name"),
                    @JoinColumn(name="school_term", referencedColumnName = "school_term"),
                    @JoinColumn(name="school_year", referencedColumnName = "school_year")
            },
            inverseJoinColumns = {@JoinColumn(name="student_id", referencedColumnName = "id")}
    )
    private Set<Student> takenBy;
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private Set<Announcement> announcements;

}
