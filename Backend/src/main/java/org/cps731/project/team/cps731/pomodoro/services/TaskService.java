package org.cps731.project.team.cps731.pomodoro.services;

import org.cps731.project.team.cps731.pomodoro.data.model.task.Task;
import org.cps731.project.team.cps731.pomodoro.data.model.user.UserType;
import org.cps731.project.team.cps731.pomodoro.data.repo.assignment.AssignmentRepo;
import org.cps731.project.team.cps731.pomodoro.data.repo.task.TaskRepo;
import org.cps731.project.team.cps731.pomodoro.data.repo.user.StudentRepo;
import org.cps731.project.team.cps731.pomodoro.data.repo.user.UserRepo;
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
    private final StudentRepo studentRepo;
    private final UserRepo userRepo;
    private final AssignmentRepo assignmentRepo;

    @Autowired
    public TaskService(TaskRepo taskRepo, StudentRepo studentRepo, UserRepo userRepo, AssignmentRepo assignmentRepo) {
        this.taskRepo = taskRepo;
        this.studentRepo = studentRepo;
        this.userRepo = userRepo;
        this.assignmentRepo = assignmentRepo;
    }

    public List<Task> getAllTasks() {
        return taskRepo.findAll();
    }

    public Task getTaskById(Long id) {
        var task = taskRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        var currentUserID = SecurityUtil.getAuthenticatedUserID();
        if (!task.getOwner().getID().equals(currentUserID)) {
            throw new AuthorizationDeniedException(
                    "Cannot access this task",
                    new AuthorizationDecision(false)
            );
        }
        return task;
    }

    public Set<Task> getTaskByState(Long ownerId, TaskState state) {
        return taskRepo.findAllByOwnerIDAndStateIsIn(ownerId, Set.of(state));
    }

    public Set<Task> getAllTasksByOwnerID(Long ownerId) {
        return taskRepo.findAllByOwnerID(ownerId);
    }

    public Set<Task> getTaskByStateAndIssueTime(Long ownerId, TaskState state, Timestamp issueTime) {
        return taskRepo.findAllByOwnerIDAndStateIsInAndDerivedFrom_Announcement_IssueTimeAfter(ownerId, Set.of(state), issueTime);
    }
    public Task createTask(TaskDTO task, Long assignmentID) {
        var userID = SecurityUtil.getAuthenticatedUserID();
        var user = userRepo.findById(userID).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!user.getUserType().equals(UserType.STUDENT)) {
            throw new AuthorizationDeniedException(
                    "Only students can create tasks",
                    new AuthorizationDecision(false)
            );
        }
        var student = studentRepo.findById(userID).orElseThrow(() -> new IllegalArgumentException("Student not found"));
        var assignment = assignmentRepo.findById(assignmentID).orElseThrow(() -> new IllegalArgumentException("Assignment not found"));

        return taskRepo.save(new Task(
                task.getTaskName(),
                new Timestamp(task.getTaskDate().getTime()),
                task.getTaskStatus() != null ? task.getTaskStatus() : TaskState.TODO,
                task.getTaskPriority(),
                student,
                assignment
        ));
    }

    public Task updateTask(Long id, TaskDTO task) {
        var userID = SecurityUtil.getAuthenticatedUserID();
        Task existingTask = taskRepo.findById(id).orElseThrow();
        if (!existingTask.getOwner().getID().equals(userID)) {
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
        if (!existingTask.getOwner().getID().equals(userID)) {
            throw new AuthorizationDeniedException(
                    "Cannot edit a task you do not own",
                    new AuthorizationDecision(false)
            );
        }
        existingTask.setState(state);
        return taskRepo.save(existingTask);
    }

    public void deleteTask(Long id) {
        var userID = SecurityUtil.getAuthenticatedUserID();
        var task = taskRepo.findById(id).orElseThrow();
        if (!task.getOwner().getID().equals(userID)) {
            throw new AuthorizationDeniedException(
                    "Cannot delete a task you do not own",
                    new AuthorizationDecision(false)
            );
        }
        taskRepo.deleteById(id);
    }
}