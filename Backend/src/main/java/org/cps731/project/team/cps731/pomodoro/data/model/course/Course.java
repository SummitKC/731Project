package org.cps731.project.team.cps731.pomodoro.data.model.course;

import jakarta.persistence.*;
import org.cps731.project.team.cps731.pomodoro.data.model.announcement.Announcement;
import org.cps731.project.team.cps731.pomodoro.data.model.user.Professor;
import org.cps731.project.team.cps731.pomodoro.data.model.user.Student;

import java.util.Set;

@Entity
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
                    @JoinColumn(name = "course_name", referencedColumnName = "course_name"),
                    @JoinColumn(name = "school_term", referencedColumnName = "school_term"),
                    @JoinColumn(name = "school_year", referencedColumnName = "school_year")
            },
            inverseJoinColumns = {@JoinColumn(name = "student_id", referencedColumnName = "id")}
    )
    private Set<Student> takenBy;
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private Set<Announcement> announcements;

    public Course(CourseID courseID, Boolean archived, Professor createdBy, Set<Student> takenBy, Set<Announcement> announcements) {
        this.courseID = courseID;
        this.archived = archived;
        this.createdBy = createdBy;
        this.takenBy = takenBy;
        this.announcements = announcements;
    }

    public Course(CourseID courseID, Boolean archived, Professor createdBy) {
        this.courseID = courseID;
        this.archived = archived;
        this.createdBy = createdBy;
    }

    public Course() {
    }

    public static CourseBuilder builder() {
        return new CourseBuilder();
    }

    public CourseID getCourseID() {
        return this.courseID;
    }

    public Boolean getArchived() {
        return this.archived;
    }

    public Professor getCreatedBy() {
        return this.createdBy;
    }

    public Set<Student> getTakenBy() {
        return this.takenBy;
    }

    public Set<Announcement> getAnnouncements() {
        return this.announcements;
    }

    public void setCourseID(CourseID courseID) {
        this.courseID = courseID;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public void setCreatedBy(Professor createdBy) {
        this.createdBy = createdBy;
    }

    public void setTakenBy(Set<Student> takenBy) {
        this.takenBy = takenBy;
    }

    public void setAnnouncements(Set<Announcement> announcements) {
        this.announcements = announcements;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Course;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Course)) return false;
        final Course other = (Course) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$courseID = this.getCourseID();
        final Object other$courseID = other.getCourseID();
        if (this$courseID == null ? other$courseID != null : !this$courseID.equals(other$courseID)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $courseID = this.getCourseID();
        result = result * PRIME + ($courseID == null ? 43 : $courseID.hashCode());
        return result;
    }

    public static class CourseBuilder {
        private CourseID courseID;
        private Boolean archived;
        private Professor createdBy;
        private Set<Student> takenBy;
        private Set<Announcement> announcements;

        CourseBuilder() {
        }

        public CourseBuilder courseID(CourseID courseID) {
            this.courseID = courseID;
            return this;
        }

        public CourseBuilder archived(Boolean archived) {
            this.archived = archived;
            return this;
        }

        public CourseBuilder createdBy(Professor createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public CourseBuilder takenBy(Set<Student> takenBy) {
            this.takenBy = takenBy;
            return this;
        }

        public CourseBuilder announcements(Set<Announcement> announcements) {
            this.announcements = announcements;
            return this;
        }

        public Course build() {
            return new Course(this.courseID, this.archived, this.createdBy, this.takenBy, this.announcements);
        }

        public String toString() {
            return "Course.CourseBuilder(courseID=" + this.courseID + ", archived=" + this.archived + ", createdBy=" + this.createdBy + ", takenBy=" + this.takenBy + ", announcements=" + this.announcements + ")";
        }
    }
}
