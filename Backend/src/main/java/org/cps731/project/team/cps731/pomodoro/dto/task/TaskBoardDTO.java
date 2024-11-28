package org.cps731.project.team.cps731.pomodoro.dto.task;

import java.util.Set;

public class TaskBoardDTO {

    private Set<TaskDTO> todoTasks;
    private Set<TaskDTO> inProgressTasks;
    private Set<TaskDTO> reviewingTasks;
    private Set<TaskDTO> completedTasks;

    public TaskBoardDTO(Set<TaskDTO> todoTasks, Set<TaskDTO> inProgressTasks, Set<TaskDTO> reviewingTasks, Set<TaskDTO> completedTasks) {
        this.todoTasks = todoTasks;
        this.inProgressTasks = inProgressTasks;
        this.reviewingTasks = reviewingTasks;
        this.completedTasks = completedTasks;
    }

    public TaskBoardDTO() {
    }

    public Set<TaskDTO> getTodoTasks() {
        return this.todoTasks;
    }

    public Set<TaskDTO> getInProgressTasks() {
        return this.inProgressTasks;
    }

    public Set<TaskDTO> getReviewingTasks() {
        return this.reviewingTasks;
    }

    public Set<TaskDTO> getCompletedTasks() {
        return this.completedTasks;
    }

    public void setTodoTasks(Set<TaskDTO> todoTasks) {
        this.todoTasks = todoTasks;
    }

    public void setInProgressTasks(Set<TaskDTO> inProgressTasks) {
        this.inProgressTasks = inProgressTasks;
    }

    public void setReviewingTasks(Set<TaskDTO> reviewingTasks) {
        this.reviewingTasks = reviewingTasks;
    }

    public void setCompletedTasks(Set<TaskDTO> completedTasks) {
        this.completedTasks = completedTasks;
    }
}
