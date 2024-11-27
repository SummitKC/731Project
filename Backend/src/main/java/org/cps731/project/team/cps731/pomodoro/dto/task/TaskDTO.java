package org.cps731.project.team.cps731.pomodoro.dto.task;

import org.cps731.project.team.cps731.pomodoro.data.model.task.Task;
import org.cps731.project.team.cps731.pomodoro.data.model.task.TaskPriority;
import org.cps731.project.team.cps731.pomodoro.data.model.task.TaskState;

import java.util.Date;

public class TaskDTO {

    private Long id;
    private String taskName;
    private TaskState taskStatus;
    private TaskPriority taskPriority;
    private Date taskDate;

    public TaskDTO(Task task) {
        id = task.getID();
        taskStatus = task.getState();
        taskName = task.getName();
        taskStatus = task.getState();
        taskPriority = task.getPriority();
        taskDate = task.getPlannedDueDate();
    }

    public TaskDTO() {
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public TaskState getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskState taskStatus) {
        this.taskStatus = taskStatus;
    }

    public TaskPriority getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(TaskPriority taskPriority) {
        this.taskPriority = taskPriority;
    }

    public Date getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(Date taskDate) {
        this.taskDate = taskDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
