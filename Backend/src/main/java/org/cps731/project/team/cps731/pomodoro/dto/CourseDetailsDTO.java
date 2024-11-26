package org.cps731.project.team.cps731.pomodoro.dto;

import org.cps731.project.team.cps731.pomodoro.data.model.course.CourseID;

import java.util.Set;


public class CourseDetailsDTO {
    private CourseID courseInfo;
    private String professor;
    private Set<AnnouncementDTO> announcements;
    private boolean isArchived;

    public CourseDetailsDTO(CourseID courseInfo, String professor, Set<AnnouncementDTO> announcements, boolean isArchived) {
        this.courseInfo = courseInfo;
        this.professor = professor;
        this.announcements = announcements;
        this.isArchived = isArchived;
    }

    public CourseDetailsDTO() {
    }

    public CourseID getCourseInfo() {
        return this.courseInfo;
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

    public void setCourseInfo(CourseID courseInfo) {
        this.courseInfo = courseInfo;
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
}
