package org.cps731.project.team.cps731.pomodoro.dto.course;

import org.cps731.project.team.cps731.pomodoro.data.model.assignment.Assignment;
import org.cps731.project.team.cps731.pomodoro.data.model.course.Course;
import org.cps731.project.team.cps731.pomodoro.dto.announcement.AnnouncementDTO;
import org.cps731.project.team.cps731.pomodoro.dto.assignment.AssignmentDTO;

import java.util.List;

public class FullCourseInfoDTO {

    private CourseDTO course;
    private List<AnnouncementDTO> announcements;
    private List<AssignmentDTO> assignments;
    private String professor;

    public FullCourseInfoDTO(Course course, List<Assignment> assignments) {
        this.course = new CourseDTO(course);
        announcements = course.getAnnouncements().stream().map(AnnouncementDTO::new).toList();
        this.assignments = assignments.stream().map(AssignmentDTO::new).toList();
        professor = course.getCreatedBy().getUser().getName();
    }

    public FullCourseInfoDTO() {
    }

    public CourseDTO getCourse() {
        return this.course;
    }

    public List<AnnouncementDTO> getAnnouncements() {
        return this.announcements;
    }

    public List<AssignmentDTO> getAssignments() {
        return this.assignments;
    }

    public String getProfessor() {
        return this.professor;
    }

    public void setCourse(CourseDTO course) {
        this.course = course;
    }

    public void setAnnouncements(List<AnnouncementDTO> announcements) {
        this.announcements = announcements;
    }

    public void setAssignments(List<AssignmentDTO> assignments) {
        this.assignments = assignments;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof FullCourseInfoDTO)) return false;
        final FullCourseInfoDTO other = (FullCourseInfoDTO) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$course = this.getCourse();
        final Object other$course = other.getCourse();
        if (this$course == null ? other$course != null : !this$course.equals(other$course)) return false;
        final Object this$announcements = this.getAnnouncements();
        final Object other$announcements = other.getAnnouncements();
        if (this$announcements == null ? other$announcements != null : !this$announcements.equals(other$announcements))
            return false;
        final Object this$assignments = this.getAssignments();
        final Object other$assignments = other.getAssignments();
        if (this$assignments == null ? other$assignments != null : !this$assignments.equals(other$assignments))
            return false;
        final Object this$professor = this.getProfessor();
        final Object other$professor = other.getProfessor();
        if (this$professor == null ? other$professor != null : !this$professor.equals(other$professor)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof FullCourseInfoDTO;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $course = this.getCourse();
        result = result * PRIME + ($course == null ? 43 : $course.hashCode());
        final Object $announcements = this.getAnnouncements();
        result = result * PRIME + ($announcements == null ? 43 : $announcements.hashCode());
        final Object $assignments = this.getAssignments();
        result = result * PRIME + ($assignments == null ? 43 : $assignments.hashCode());
        final Object $professor = this.getProfessor();
        result = result * PRIME + ($professor == null ? 43 : $professor.hashCode());
        return result;
    }

    public String toString() {
        return "FullCourseInfoDTO(course=" + this.getCourse() + ", announcements=" + this.getAnnouncements() + ", assignments=" + this.getAssignments() + ", professor=" + this.getProfessor() + ")";
    }
}
