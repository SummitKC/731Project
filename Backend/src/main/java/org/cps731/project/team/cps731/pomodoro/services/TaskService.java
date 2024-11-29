package org.cps731.project.team.cps731.pomodoro.services;

import org.cps731.project.team.cps731.pomodoro.data.model.task.Task;
import org.cps731.project.team.cps731.pomodoro.data.repo.task.TaskRepo;
import org.cps731.project.team.cps731.pomodoro.dto.task.TaskDTO;
import org.cps731.project.team.cps731.pomodoro.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.cps731.project.team.cps731.pomodoro.data.model.task.TaskState;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Service
public class TaskService {

    private final TaskRepo taskRepo;

    @Autowired
    public TaskService(TaskRepo taskRepo) {
        this.taskRepo = taskRepo;
    }

    public List<Task> getAllTasks() {
        return taskRepo.findAll();
    }

    public Task getTaskById(Long id) {
        return taskRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Task not found"));
    }

    public Set<Task> getTaskByState(Long ownerId, TaskState state) {
        return taskRepo.findAllByOwnerStudentIDAndStateIsIn(ownerId, Set.of(state));
    }

    public Set<Task> getTaskByStateAndIssueTime(Long ownerId, TaskState state, Timestamp issueTime) {
        return taskRepo.findAllByOwnerStudentIDAndStateIsInAndDerivedFrom_Announcement_IssueTimeAfter(ownerId, Set.of(state), issueTime);
    }
    public Task createTask(Task task) {
        return taskRepo.save(task);
    }

    public Task updateTask(Long id, TaskDTO task) {
        var userID = SecurityUtil.getAuthenticatedUserID();
        Task existingTask = taskRepo.findById(id).orElseThrow();
        if (!existingTask.getOwner().getStudentID().equals(userID)) {
            throw new AuthorizationDeniedException(
                    "Cannot edit a task you do not own",
                    new AuthorizationDecision(false)
            );
        }
        existingTask.updateFromTaskDTO(task);
        return taskRepo.save(existingTask);
    }

    public Task changeTaskState(Long id, TaskState state) {
        var userID = SecurityUtil.getAuthenticatedUserID();
        var existingTask = taskRepo.findById(id).orElseThrow();
        if (!existingTask.getOwner().getStudentID().equals(userID)) {
            throw new AuthorizationDeniedException(
                    "Cannot edit a task you do not own",
                    new AuthorizationDecision(false)
            );
        }
        existingTask.setState(state);
        return taskRepo.save(existingTask);
    }

    public void deleteTask(Long id) {
        taskRepo.deleteById(id);
    }
}