package org.cps731.project.team.cps731.pomodoro.data.model.course;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class CourseID {

    @Column(name = "course_name")
    private String name;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "school_term")
    private Term term;
    @Column(name = "school_year")
    private Integer year;

    public CourseID(String name, Term term, Integer year) {
        this.name = name;
        this.term = term;
        this.year = year;
    }

    public CourseID() {
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

    public void setName(String name) {
        this.name = name;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof CourseID)) return false;
        final CourseID other = (CourseID) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$term = this.getTerm();
        final Object other$term = other.getTerm();
        if (this$term == null ? other$term != null : !this$term.equals(other$term)) return false;
        final Object this$year = this.getYear();
        final Object other$year = other.getYear();
        if (this$year == null ? other$year != null : !this$year.equals(other$year)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof CourseID;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $term = this.getTerm();
        result = result * PRIME + ($term == null ? 43 : $term.hashCode());
        final Object $year = this.getYear();
        result = result * PRIME + ($year == null ? 43 : $year.hashCode());
        return result;
    }

}
