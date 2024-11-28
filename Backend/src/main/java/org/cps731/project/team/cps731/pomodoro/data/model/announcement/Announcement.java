package org.cps731.project.team.cps731.pomodoro.data.model.announcement;

import jakarta.persistence.*;
import org.cps731.project.team.cps731.pomodoro.data.model.course.Course;
import org.cps731.project.team.cps731.pomodoro.dto.announcement.AnnouncementDTO;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Entity
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ID;
    private String title;
    @Column(name = "issue_time")
    private Timestamp issueTime;
    private String description;
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "course_name", referencedColumnName = "course_name"),
            @JoinColumn(name = "school_term", referencedColumnName = "school_term"),
            @JoinColumn(name = "school_year", referencedColumnName = "school_year")
    })
    private Course course;

    public Announcement(String title, Timestamp issueTime, String description, Course course) {
        this.title = title;
        this.issueTime = issueTime;
        this.description = description;
        this.course = course;
    }

    public Announcement(AnnouncementDTO announcementDTO, Course course) throws ParseException {
        this.title = announcementDTO.getAnnouncementHeader();
        this.description = announcementDTO.getAnnouncementDescription();
        this.issueTime = new Timestamp(new SimpleDateFormat("MM/dd/yyyy").parse(announcementDTO.getAnnouncementPostDate()).getTime());
        this.course = course;
    }

    public Announcement() {
    }

    public static AnnouncementBuilder builder() {
        return new AnnouncementBuilder();
    }

    public Long getID() {
        return this.ID;
    }

    public String getTitle() {
        return this.title;
    }

    public Timestamp getIssueTime() {
        return this.issueTime;
    }

    public String getDescription() {
        return this.description;
    }

    public Course getCourse() {
        return this.course;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIssueTime(Timestamp issueTime) {
        this.issueTime = issueTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Announcement)) return false;
        final Announcement other = (Announcement) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$ID = this.getID();
        final Object other$ID = other.getID();
        if (this$ID == null ? other$ID != null : !this$ID.equals(other$ID)) return false;
        final Object this$course = this.getCourse();
        final Object other$course = other.getCourse();
        if (this$course == null ? other$course != null : !this$course.equals(other$course)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Announcement;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $ID = this.getID();
        result = result * PRIME + ($ID == null ? 43 : $ID.hashCode());
        final Object $course = this.getCourse();
        result = result * PRIME + ($course == null ? 43 : $course.hashCode());
        return result;
    }

    public static class AnnouncementBuilder {
        private String title;
        private Timestamp issueTime;
        private String description;
        private Course course;

        AnnouncementBuilder() {
        }

        public AnnouncementBuilder title(String title) {
            this.title = title;
            return this;
        }

        public AnnouncementBuilder issueTime(Timestamp issueTime) {
            this.issueTime = issueTime;
            return this;
        }

        public AnnouncementBuilder description(String description) {
            this.description = description;
            return this;
        }

        public AnnouncementBuilder course(Course course) {
            this.course = course;
            return this;
        }

        public Announcement build() {
            return new Announcement(this.title, this.issueTime, this.description, this.course);
        }

        public String toString() {
            return "Announcement.AnnouncementBuilder(title=" + this.title + ", issueTime=" + this.issueTime + ", description=" + this.description + ", course=" + this.course + ")";
        }
    }
}
