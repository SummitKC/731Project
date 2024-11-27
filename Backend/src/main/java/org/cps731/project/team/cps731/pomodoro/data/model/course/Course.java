package org.cps731.project.team.cps731.pomodoro.data.model.course;

import jakarta.persistence.*;
import org.cps731.project.team.cps731.pomodoro.data.model.announcement.Announcement;
import org.cps731.project.team.cps731.pomodoro.data.model.user.Professor;
import org.cps731.project.team.cps731.pomodoro.data.model.user.Student;

import java.util.Set;

@Entity
public class Course {

    @Id
    @Column(name = "code_code")
    private String courseCode;
    @Column(name = "course_name")
    private String name;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "school_term")
    private Term term;
    @Column(name = "school_year")
    private Integer year;
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

    public Course(String courseCode, String name, Term term, Integer year, Boolean archived, Professor createdBy) {
        this.courseCode = courseCode;
        this.name = name;
        this.term = term;
        this.year = year;
        this.archived = archived;
        this.createdBy = createdBy;
    }

    public Course() {
    }

    public Course(String courseCode, String name, Term term, Integer year, Boolean archived, Professor createdBy, Set<Student> takenBy, Set<Announcement> announcements) {
        this.courseCode = courseCode;
        this.name = name;
        this.term = term;
        this.year = year;
        this.archived = archived;
        this.createdBy = createdBy;
        this.takenBy = takenBy;
        this.announcements = announcements;
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

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Course)) return false;
        final Course other = (Course) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$courseCode = this.getCourseCode();
        final Object other$courseCode = other.getCourseCode();
        if (this$courseCode == null ? other$courseCode != null : !this$courseCode.equals(other$courseCode))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Course;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $courseCode = this.getCourseCode();
        result = result * PRIME + ($courseCode == null ? 43 : $courseCode.hashCode());
        return result;
    }
}
