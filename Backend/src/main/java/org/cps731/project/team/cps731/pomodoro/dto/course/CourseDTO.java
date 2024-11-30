package org.cps731.project.team.cps731.pomodoro.dto.course;

import org.cps731.project.team.cps731.pomodoro.data.model.course.Course;
import org.cps731.project.team.cps731.pomodoro.data.model.course.Term;

public class CourseDTO {

    private String courseCode;
    private String name;
    private Term term;
    private Integer year;
    private boolean archived;

    public CourseDTO(String courseCode, String name, Term term, Integer year, boolean archived) {
        this.courseCode = courseCode;
        this.name = name;
        this.term = term;
        this.year = year;
        this.archived = archived;
    }

    public CourseDTO(Course course) {
        courseCode = course.getCourseCode();
        name = course.getName();
        term = course.getTerm();
        year = course.getYear();
        archived = course.getArchived();
    }

    public CourseDTO() {
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getName() {
        return this.name;
    }

    public Term getTerm() {
        return this.term;
    }

    public Integer getYear() {
        return this.year;
    }

    public boolean isArchived() {
        return this.archived;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof CourseDTO)) return false;
        final CourseDTO other = (CourseDTO) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$courseCode = this.getCourseCode();
        final Object other$courseCode = other.getCourseCode();
        if (this$courseCode == null ? other$courseCode != null : !this$courseCode.equals(other$courseCode))
            return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$term = this.getTerm();
        final Object other$term = other.getTerm();
        if (this$term == null ? other$term != null : !this$term.equals(other$term)) return false;
        final Object this$year = this.getYear();
        final Object other$year = other.getYear();
        if (this$year == null ? other$year != null : !this$year.equals(other$year)) return false;
        if (this.isArchived() != other.isArchived()) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof CourseDTO;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $courseCode = this.getCourseCode();
        result = result * PRIME + ($courseCode == null ? 43 : $courseCode.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $term = this.getTerm();
        result = result * PRIME + ($term == null ? 43 : $term.hashCode());
        final Object $year = this.getYear();
        result = result * PRIME + ($year == null ? 43 : $year.hashCode());
        result = result * PRIME + (this.isArchived() ? 79 : 97);
        return result;
    }

    public String toString() {
        return "CourseDTO(courseCode=" + this.getCourseCode() + ", name=" + this.getName() + ", term=" + this.getTerm() + ", year=" + this.getYear() + ", archived=" + this.isArchived() + ")";
    }
}
