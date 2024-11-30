package org.cps731.project.team.cps731.pomodoro.dto.student;

import org.cps731.project.team.cps731.pomodoro.dto.course.CourseDTO;
import org.cps731.project.team.cps731.pomodoro.dto.task.TaskDTO;

import java.util.Set;

public class StudentDashboardDTO {

    private Set<CourseDTO> courses;
    private Set<TaskDTO> upcomingTasks;
    private Set<TaskDTO> inProgressTasks;

    public StudentDashboardDTO(Set<CourseDTO> courses, Set<TaskDTO> upcomingTasks, Set<TaskDTO> inProgressTasks) {
        this.courses = courses;
        this.upcomingTasks = upcomingTasks;
        this.inProgressTasks = inProgressTasks;
    }

    public StudentDashboardDTO() {
    }

    public Set<CourseDTO> getCourses() {
        return this.courses;
    }

    public Set<TaskDTO> getUpcomingTasks() {
        return this.upcomingTasks;
    }

    public Set<TaskDTO> getInProgressTasks() {
        return this.inProgressTasks;
    }

    public void setCourses(Set<CourseDTO> courses) {
        this.courses = courses;
    }

    public void setUpcomingTasks(Set<TaskDTO> upcomingTasks) {
        this.upcomingTasks = upcomingTasks;
    }

    public void setInProgressTasks(Set<TaskDTO> inProgressTasks) {
        this.inProgressTasks = inProgressTasks;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof StudentDashboardDTO)) return false;
        final StudentDashboardDTO other = (StudentDashboardDTO) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$courses = this.getCourses();
        final Object other$courses = other.getCourses();
        if (this$courses == null ? other$courses != null : !this$courses.equals(other$courses)) return false;
        final Object this$upcomingTasks = this.getUpcomingTasks();
        final Object other$upcomingTasks = other.getUpcomingTasks();
        if (this$upcomingTasks == null ? other$upcomingTasks != null : !this$upcomingTasks.equals(other$upcomingTasks))
            return false;
        final Object this$inProgressTasks = this.getInProgressTasks();
        final Object other$inProgressTasks = other.getInProgressTasks();
        if (this$inProgressTasks == null ? other$inProgressTasks != null : !this$inProgressTasks.equals(other$inProgressTasks))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof StudentDashboardDTO;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $courses = this.getCourses();
        result = result * PRIME + ($courses == null ? 43 : $courses.hashCode());
        final Object $upcomingTasks = this.getUpcomingTasks();
        result = result * PRIME + ($upcomingTasks == null ? 43 : $upcomingTasks.hashCode());
        final Object $inProgressTasks = this.getInProgressTasks();
        result = result * PRIME + ($inProgressTasks == null ? 43 : $inProgressTasks.hashCode());
        return result;
    }

    public String toString() {
        return "StudentDashboardDTO(courses=" + this.getCourses() + ", upcomingTasks=" + this.getUpcomingTasks() + ", inProgressTasks=" + this.getInProgressTasks() + ")";
    }
}
