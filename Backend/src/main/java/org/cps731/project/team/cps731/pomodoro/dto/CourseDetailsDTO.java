package org.cps731.project.team.cps731.pomodoro.dto;

import org.cps731.project.team.cps731.pomodoro.data.model.course.Course;
import org.cps731.project.team.cps731.pomodoro.data.model.course.Term;

import java.util.Set;
import java.util.stream.Collectors;

public class CourseDetailsDTO {
    private String courseCode;
    private String courseName;
    private Term term;
    private String professor;
    private Set<AnnouncementDTO> announcements;
    private Set<AssignmentDTO> assignments;
    private boolean isArchived;

    public CourseDetailsDTO() {
    }

    public CourseDetailsDTO(Course course, Set<AssignmentDTO> assignments) {
        courseCode = course.getCourseCode();
        courseName = course.getName();
        term = course.getTerm();
        professor = course.getCreatedBy().getUser().getEmail();
        announcements = course.getAnnouncements().stream().map(AnnouncementDTO::new).collect(Collectors.toSet());
        this.assignments = assignments;
        isArchived = course.getArchived();
    }

    public String getProfessor() {
        return this.professor;
    }

    public Set<AnnouncementDTO> getAnnouncements() {
        return this.announcements;
    }

    public boolean isArchived() {
        return this.isArchived;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public void setAnnouncements(Set<AnnouncementDTO> announcements) {
        this.announcements = announcements;
    }

    public void setArchived(boolean isArchived) {
        this.isArchived = isArchived;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

    public Set<AssignmentDTO> getAssignments() {
        return assignments;
    }

    public void setAssignments(Set<AssignmentDTO> assignments) {
        this.assignments = assignments;
    }
}
