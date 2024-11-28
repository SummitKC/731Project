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
}
