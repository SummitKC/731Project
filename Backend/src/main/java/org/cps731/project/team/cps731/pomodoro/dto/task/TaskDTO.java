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

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof TaskDTO)) return false;
        final TaskDTO other = (TaskDTO) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$taskName = this.getTaskName();
        final Object other$taskName = other.getTaskName();
        if (this$taskName == null ? other$taskName != null : !this$taskName.equals(other$taskName)) return false;
        final Object this$taskStatus = this.getTaskStatus();
        final Object other$taskStatus = other.getTaskStatus();
        if (this$taskStatus == null ? other$taskStatus != null : !this$taskStatus.equals(other$taskStatus))
            return false;
        final Object this$taskPriority = this.getTaskPriority();
        final Object other$taskPriority = other.getTaskPriority();
        if (this$taskPriority == null ? other$taskPriority != null : !this$taskPriority.equals(other$taskPriority))
            return false;
        final Object this$taskDate = this.getTaskDate();
        final Object other$taskDate = other.getTaskDate();
        if (this$taskDate == null ? other$taskDate != null : !this$taskDate.equals(other$taskDate)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof TaskDTO;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $taskName = this.getTaskName();
        result = result * PRIME + ($taskName == null ? 43 : $taskName.hashCode());
        final Object $taskStatus = this.getTaskStatus();
        result = result * PRIME + ($taskStatus == null ? 43 : $taskStatus.hashCode());
        final Object $taskPriority = this.getTaskPriority();
        result = result * PRIME + ($taskPriority == null ? 43 : $taskPriority.hashCode());
        final Object $taskDate = this.getTaskDate();
        result = result * PRIME + ($taskDate == null ? 43 : $taskDate.hashCode());
        return result;
    }
}
