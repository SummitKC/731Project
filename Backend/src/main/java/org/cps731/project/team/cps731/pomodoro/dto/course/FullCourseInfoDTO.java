package org.cps731.project.team.cps731.pomodoro.dto.course;

import org.cps731.project.team.cps731.pomodoro.data.model.assignment.Assignment;
import org.cps731.project.team.cps731.pomodoro.data.model.course.Course;
import org.cps731.project.team.cps731.pomodoro.data.model.course.Term;
import org.cps731.project.team.cps731.pomodoro.dto.assignment.AssignmentDTO;
import org.cps731.project.team.cps731.pomodoro.dto.announcement.AnnouncementDTO;

import java.util.List;

public class FullCourseInfoDTO {

    private String courseName;
    private Term courseTerm;
    private Integer year;
    private List<AnnouncementDTO> announcements;
    private List<AssignmentDTO> assignments;

    public FullCourseInfoDTO(Course course, List<Assignment> assignments) {
        courseName = course.getName();
        courseTerm = course.getTerm();
        year = course.getYear();
        announcements = course.getAnnouncements().stream().map(AnnouncementDTO::new).toList();
        this.assignments = assignments.stream().map(AssignmentDTO::new).toList();
    }

    public FullCourseInfoDTO() {
    }

    public String getCourseName() {
        return this.courseName;
    }

    public Term getCourseTerm() {
        return this.courseTerm;
    }

    public Integer getYear() {
        return this.year;
    }

    public List<AnnouncementDTO> getAnnouncements() {
        return this.announcements;
    }

    public List<AssignmentDTO> getAssignments() {
        return this.assignments;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setCourseTerm(Term courseTerm) {
        this.courseTerm = courseTerm;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setAnnouncements(List<AnnouncementDTO> announcements) {
        this.announcements = announcements;
    }

    public void setAssignments(List<AssignmentDTO> assignments) {
        this.assignments = assignments;
    }

}
