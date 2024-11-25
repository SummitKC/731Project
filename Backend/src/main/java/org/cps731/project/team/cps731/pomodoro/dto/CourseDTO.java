package org.cps731.project.team.cps731.pomodoro.dto;

import org.cps731.project.team.cps731.pomodoro.data.model.course.Course;
import org.cps731.project.team.cps731.pomodoro.data.model.course.Term;

public class CourseDTO {

    private String name;
    private Term term;
    private Integer year;
    private boolean archived;

    public CourseDTO(String name, Term term, Integer year, boolean archived) {
        this.name = name;
        this.term = term;
        this.year = year;
        this.archived = archived;
    }

    public CourseDTO(Course course) {
        name = course.getCourseID().getName();
        term = course.getCourseID().getTerm();
        year = course.getCourseID().getYear();
        archived = course.getArchived();
    }

    public CourseDTO() {
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
}
